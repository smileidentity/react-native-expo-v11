# SmileID React Native (Expo) SDK (v11) — Agents Guide

Guidance for AI coding agents working on the Smile ID React Native Expo SDK v11. Humans welcome;
tone optimised for tools.

## What This Repo Is

**`@smile_identity/react-native-expo`** — an **Expo Module** wrapping the native Smile ID SDKs.
The flows, capture UI, camera and ML all live natively; this package exposes them to JS through
Expo Modules. Smile ID provides digital KYC, identity verification, and onboarding across
Africa; this is customer-facing product code.

**This is the critical fact about this repo:** it is *not* where behaviour lives. A capture bug,
a threshold change, or a liveness fix belongs in `smileidentity/android-v11` or
`smileidentity/ios-sdk` — not here. This package passes configuration down and results back,
faithfully.

**This is a sibling of, not a fork of, `@smile_identity/react-native`.** That package is a
legacy-bridge RN library; this one is an Expo Module with its own module surface. Changes often
need mirroring in both — check before assuming a fix here is enough.

**v11 is the line partners are on today.** v12 is a separate, ground-up SDK where the Expo
package is TypeScript-first and owns its own UI. Do not port v12 patterns here.

## Golden Rules

- These rules encode decisions already made — don't relitigate them per change. If a rule
  genuinely shouldn't apply, say so and ask; never silently deviate.
- Precedence when sources disagree: **this file > existing code**. Code that violates a rule is
  tracked legacy debt, not license to imitate.
- Never claim something works or passes unless you actually ran it; list exactly what you
  couldn't run.
- Any partner-facing change adds a plain-language bullet to `CHANGELOG.md` in the same change.
- When a new convention is agreed, record it in this file in the same change.
- **Fix behaviour natively, not in TypeScript.** Wrapping around a native bug here creates
  divergence between platforms instead of resolving it.

## Commands

```bash
yarn lint                     # ESLint
yarn expo-module typecheck    # typecheck (Expo Modules toolchain, not plain tsc)
yarn test
yarn build                    # expo-module build
yarn open:ios-expo            # open the iOS example
```

**CI map:** per-PR gate = `ci.yml` — `yarn lint`, `yarn expo-module typecheck`, tests, and an
Android `./gradlew --no-daemon assembleDebug`. Also `audit.yml` and `semgrep.yml`. Publishing is
CI's job — never publish locally.

Note the typecheck goes through **`expo-module`**, not bare `tsc`; running `tsc` directly can
give different results because the Expo Modules tsconfig is layered.

## Architecture

- `src/index.ts` — the public surface
- `src/SmileIDExpoModule.ts` — the Expo Module interface
- `src/products/` — one entry per product flow
- `src/types/` — shared TS types
- `expo-module.config.json` — declares the native modules to Expo autolinking. **Changing this
  changes how host apps link the package**; treat edits as an ABI-level change.
- `android/` — Kotlin glue, group `com.smileidentity.react.expo`
- `ios/SmileIDExpo.podspec` — iOS glue

## Native Pins — bump both together

| platform | file | pin |
|---|---|---|
| Android | `android/build.gradle` | `com.smileidentity:android-sdk:<version>` |
| iOS | `ios/SmileIDExpo.podspec` | `s.dependency 'SmileID', '<version>'` |

Bumping one without the other ships a package whose two platforms behave differently. Always
bump both in the same change and state the native versions in the PR.

⚠️ **The package version and the native pin move independently.** This package is versioned well
ahead of the native SDK it pins, so "we're on 11.2.x" says nothing about which native SDK
partners actually get. Read the pins, don't infer them from the package version — and when a
native fix is what a partner needs, bumping this package alone will not deliver it.

## Cross-Platform Parity

This SDK has siblings on Android, iOS, Flutter and React Native. Parity is a contract: public
type names, config fields **and their defaults**, and error-code strings stay aligned across the
wrappers, and all must agree with the natives underneath.

The closest sibling is `@smile_identity/react-native` — same platform, different module system.
A public-surface change here usually needs mirroring there. **State the parity impact in the
PR**, and mirror in the sibling wrappers if they're available locally.

Some divergences are intentional — notably ML threshold magnitudes differ between Android and
iOS because ML Kit and Vision report head rotation differently. **Never "align" those by copying
numbers across**; that breaks liveness on real devices while tests stay green.

## Conventions

- TypeScript with explicit types per product entry; `readonly` props and immutable data shapes.
- **No abbreviations**; spell out short locals (`viewModel` not `vm`); exceptions: loop counters
  and `e` for caught errors.
- **No business logic in this package.** TS configures, forwards, and surfaces results. If you
  find yourself reimplementing a validation rule or state machine that exists natively, stop —
  that's the divergence this wrapper exists to avoid.
- No `any` without a justifying comment; narrow at the module boundary rather than casting.
- Never log PII or secrets — no tokens, JWTs, signatures, images or partner params.

## Testing

- Tests run under `yarn test`; the Android example build in CI is what catches native-glue
  breakage.
- There is no visual/snapshot gate for the native views — they render natively, so UI-affecting
  changes are verified by hand on a device. Say so in the PR.
- **Every defect fix ships a test that fails before the fix**, at the tightest layer that
  reproduces it; name it in the PR.
- A test that passes and fails on the same commit is a defect in the test — fix or quarantine it,
  never add retries.

## Documentation

**Comment discipline — default to no comment.** A comment earns its place only by stating a
constraint or a WHY the code cannot express — never what the next line does. TSDoc on public API
is one clear sentence plus the params that genuinely need explaining.

Source comments stay self-contained: describe behaviour in this SDK's own terms. No comparisons
to the v12 SDKs, no file paths into other repos.

**Partner-facing docs:** any change partners can see or copy-paste — public API, config
fields/defaults, install coordinates, minimum requirements, error codes, permissions, README
quick-start — needs a matching docs update or a "Docs impact" note in the PR description.

## Definition of Done

- ⚠️ **Ask first:** new dependencies; native SDK version bumps; changes to
  `expo-module.config.json` (autolinking / ABI surface).
- 🚫 **Never:** commit secrets; log PII; publish packages; reimplement native behaviour in TS.

Before finishing any change:

- [ ] `yarn lint`, `yarn expo-module typecheck`, `yarn test` green, and `yarn build` succeeds
- [ ] Native pin bumped → **both** `android/build.gradle` and `ios/SmileIDExpo.podspec`, versions
      stated in the PR
- [ ] Public surface changed → parity impact stated; the sibling `@smile_identity/react-native`
      package mirrored
- [ ] UI-affecting change → verified on a device and stated as such
- [ ] `CHANGELOG.md` bullet for anything partner-visible
- [ ] Self-review in priority order — security (no PII/secrets in logs) → module contract (the TS
      surface matches the native module on both sides) → reliability (error paths surfaced, not
      swallowed) → architecture (no behaviour reimplemented in TS) → readability. If you can't
      describe a concrete failure scenario, don't flag it.
