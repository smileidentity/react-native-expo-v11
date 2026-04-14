import React from 'react';
import { ViewProps } from "react-native";
import { BiometricKYCParams } from "../types/SmileIDExpo.types";
import { getArchitectureComponent, detectArchitecture } from "../compat/ArchitectureDetector";

// Import both Fabric and legacy components
import LegacySmileIDBiometricKYCView from './SmileIDBiometricKYCView';
import FabricSmileIDBiometricKYCView from '../fabric/SmileIDBiometricKYCViewFabric';

interface SmileIDBiometricKYCViewProps extends ViewProps {
    params: BiometricKYCParams;
    onResult?: (result: any) => void;
    onError?: (error: any) => void;
}

/**
 * Architecture-aware Biometric KYC View component
 * Automatically uses Fabric component for New Architecture, falls back to legacy for Old Architecture
 */
export default function SmileIDBiometricKYCView(props: SmileIDBiometricKYCViewProps) {
    const architecture = detectArchitecture();
    
    // Log architecture detection for debugging
    if (__DEV__) {
        console.log(`SmileID: Using ${architecture.isNewArchitecture ? 'New' : 'Old'} Architecture`);
    }
    
    // Return appropriate component based on architecture
    const Component = getArchitectureComponent(
        FabricSmileIDBiometricKYCView,
        LegacySmileIDBiometricKYCView
    );
    
    return <Component {...props} />;
}
