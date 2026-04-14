import React from 'react';
import { requireNativeComponent } from 'react-native';
import { ViewProps } from "react-native";
import { BiometricKYCParams } from "../types/SmileIDExpo.types";

interface SmileIDBiometricKYCViewProps extends ViewProps {
    params: BiometricKYCParams;
    onResult?: (result: any) => void;
    onError?: (error: any) => void;
}

// Fabric-compatible component using requireNativeComponent
const NativeSmileIDBiometricKYCView = requireNativeComponent<SmileIDBiometricKYCViewProps>('SmileIDBiometricKYCView');

export default function SmileIDBiometricKYCView(props: SmileIDBiometricKYCViewProps) {
    return <NativeSmileIDBiometricKYCView {...props} />;
}
