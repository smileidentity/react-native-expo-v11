import React from 'react';
import { ViewProps } from "react-native";
import { BiometricKYCParams } from "../types/SmileIDExpo.types";
import { isExpoGo, isNativeModuleAvailable } from "../mock/ExpoGoDetector";
import MockSmileIDView from "../mock/MockSmileIDView";
import LegacySmileIDBiometricKYCView from './SmileIDBiometricKYCView';

interface SmileIDBiometricKYCViewProps extends ViewProps {
    params: BiometricKYCParams;
    onResult?: (result: any) => void;
    onError?: (error: any) => void;
}

/**
 * Biometric KYC View component with Expo Go mock support
 * Automatically detects Expo Go environment and provides mock functionality
 */
export default function SmileIDBiometricKYCView(props: SmileIDBiometricKYCViewProps) {
    // Check if we're in Expo Go or native module is not available
    const useMock = isExpoGo() || !isNativeModuleAvailable('SmileIDExpo');
    
    if (__DEV__) {
        console.log(`SmileID: Using ${useMock ? 'Mock' : 'Native'} implementation`);
    }
    
    if (useMock) {
        return (
            <MockSmileIDView
                {...props}
                productType="biometric-kyc"
            />
        );
    }
    
    // Use native component
    return <LegacySmileIDBiometricKYCView {...props} />;
}
