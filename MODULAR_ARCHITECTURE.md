# Modular Architecture Design

This document outlines the transition from a monolithic package to a modular, tree-shakeable architecture for the Smile ID Expo SDK.

## Architecture Overview

### Current (Monolithic)
```
@smile_identity/react-native-expo
  - All products bundled together
  - Large bundle size impact
  - No tree-shaking support
```

### New (Modular)
```
@smileid/core                    # Base functionality and types
@smileid/biometric-kyc          # Biometric verification only
@smileid/document-verification  # Document scanning only  
@smileid/smart-selfie           # Selfie verification only
```

## Benefits

### 1. **Tree Shaking Support**
- Developers only import what they need
- Reduced bundle sizes
- Faster app startup times

### 2. **Independent Versioning**
- Each product can be versioned independently
- Smaller, more focused releases
- Easier dependency management

### 3. **Better Developer Experience**
- Clear separation of concerns
- Smaller installation size per product
- Better IDE performance with smaller packages

## Usage Examples

### Before (Monolithic)
```bash
npm install @smile_identity/react-native-expo
```

```typescript
import { SmileIDBiometricKYCView, DocumentVerificationView } from '@smile_identity/react-native-expo';
// Both products bundled, even if only using one
```

### After (Modular)
```bash
# Only install what you need
npm install @smileid/core @smileid/biometric-kyc
```

```typescript
import { SmileIDBiometricKYCView } from '@smileid/biometric-kyc';
import { BiometricKYCParams } from '@smileid/core';
// Only biometric KYC functionality included in bundle
```

## Migration Guide

### For Existing Users
1. No breaking changes - monolithic package still available
2. Gradual migration path to modular packages
3. Backward compatibility maintained

### For New Users
1. Start with modular packages directly
2. Install only required products
3. Benefit from smaller bundle sizes immediately

## Package Structure

### @smileid/core
- Base types and interfaces
- Core module interface
- Utility functions
- Architecture detection
- Expo Go compatibility

### @smileid/biometric-kyc
- Biometric KYC components
- Depends on @smileid/core
- ~2MB smaller than full package

### @smileid/document-verification  
- Document verification components
- Depends on @smileid/core
- ~3MB smaller than full package

### @smileid/smart-selfie
- Smart selfie components
- Depends on @smileid/core
- ~1.5MB smaller than full package

## Build System

### Monorepo Structure
```
packages/
  core/
  biometric-kyc/
  document-verification/
  smart-selfie/
```

### Build Commands
```bash
# Build all packages
npm run build:packages

# Build individual package
npm run build:core

# Development mode for all packages
npm run dev:packages
```

## Tree Shaking Statistics

### Bundle Size Impact (Estimated)
- **Full SDK**: ~8MB
- **Biometric KYC only**: ~6MB (-25%)
- **Document Verification only**: ~5MB (-37%)
- **Smart Selfie only**: ~6.5MB (-19%)

### Web Bundle Impact
- **Full SDK**: ~2.1MB
- **Single Product**: ~1.3MB (-38%)

## Implementation Timeline

### Phase 1: Core Package
- [x] Extract core functionality
- [x] Create base types
- [x] Implement utilities

### Phase 2: Product Packages
- [x] Create biometric-kyc package
- [x] Create document-verification package  
- [x] Create smart-selfie package

### Phase 3: Migration Tools
- [ ] Create migration guide
- [ ] Add build scripts
- [ ] Update documentation

### Phase 4: Release
- [ ] Publish modular packages
- [ ] Update examples
- [ ] Community feedback

## Backward Compatibility

The monolithic package will continue to be maintained alongside the modular packages to ensure existing users are not affected.
