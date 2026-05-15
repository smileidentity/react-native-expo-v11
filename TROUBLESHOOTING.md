# Troubleshooting

Common build issues encountered when integrating the Smile ID Expo SDK, and their workarounds.

## iOS: Swift Module Interface Verification Error

If you encounter a build error like:

```
SwiftVerifyEmittedModuleInterface failed with a nonzero exit code
```

or

```
Verifying emitted module interface SmileID.swiftinterface failed
```

This is caused by Swift module interface verification with the SmileID iOS SDK. To resolve this, add the following to your `ios/Podfile` inside the `post_install` block:

```ruby
post_install do |installer|
  # ... existing post_install code ...

  # Fix SmileID Swift module interface verification error
  installer.pods_project.targets.each do |target|
    if target.name == 'SmileID'
      target.build_configurations.each do |config|
        config.build_settings['BUILD_LIBRARY_FOR_DISTRIBUTION'] = 'NO'
      end
    end
  end
end
```

Then run `pod install` again and rebuild your project.

## iOS: `fmt` consteval build error on Xcode 26 (React Native 0.81)

If `npx expo run:ios` (or `pod install` + Xcode build) fails while compiling `Pods/fmt` with errors like:

```
ios/Pods/fmt/include/fmt/format-inl.h:59:24: error:
  call to consteval function 'fmt::basic_format_string<...>::basic_format_string<FMT_COMPILE_STRING, 0>'
  is not a constant expression
    fmt::format_to(it, FMT_STRING("{}{}"), message, SEP);
```

This is **not a Smile ID issue**. It's a known incompatibility between Xcode 26's Clang and the `fmt 11.0.2` library that React Native 0.81 pulls in via `RCT-Folly`. It affects any RN 0.81 app on Xcode 26, with or without Smile ID installed. The upstream fix is in `fmt 11.1+`; until React Native ships that version, apply this workaround in your `ios/Podfile` inside the `post_install` block:

```ruby
post_install do |installer|
  # ... existing post_install code ...

  # Workaround: fmt 11.0.2 (pulled in by RCT-Folly) doesn't build on Xcode 26's Clang.
  # base.h re-defines FMT_USE_CONSTEVAL unguarded, so a -D flag won't take — patch the header.
  fmt_base = File.join(__dir__, 'Pods', 'fmt', 'include', 'fmt', 'base.h')
  if File.exist?(fmt_base)
    contents = File.read(fmt_base)
    patched = contents.gsub(
      /\#elif defined\(__cpp_consteval\)\n\#  define FMT_USE_CONSTEVAL 1\n\#elif FMT_GCC_VERSION >= 1002 \|\| FMT_CLANG_VERSION >= 1101\n\#  define FMT_USE_CONSTEVAL 1/,
      "#elif defined(__cpp_consteval)\n#  define FMT_USE_CONSTEVAL 0\n#elif FMT_GCC_VERSION >= 1002 || FMT_CLANG_VERSION >= 1101\n#  define FMT_USE_CONSTEVAL 0"
    )
    File.write(fmt_base, patched) if patched != contents
  end
end
```

The patch is idempotent and re-applies on every `pod install`. Re-run `npx expo run:ios` after adding it. You can remove this block once you upgrade to a React Native version that ships `fmt 11.1+`.
