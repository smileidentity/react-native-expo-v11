import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import { ViewProps } from "react-native";
import { BiometricKYCParams } from "../types/SmileIDExpo.types";
import { isExpoGo } from "./ExpoGoDetector";

interface MockSmileIDViewProps extends ViewProps {
    params: BiometricKYCParams;
    onResult?: (result: any) => void;
    onError?: (error: any) => void;
    productType: 'biometric-kyc' | 'document-verification' | 'smart-selfie';
}

/**
 * Mock component that simulates Smile ID functionality in Expo Go
 */
export default function MockSmileIDView({ params, onResult, onError, productType, style }: MockSmileIDViewProps) {
    const handleMockResult = () => {
        const mockResult = {
            success: true,
            result: {
                job_id: params.jobId || `mock-job-${Date.now()}`,
                user_id: params.userId || 'mock-user',
                timestamp: new Date().toISOString(),
                product_type: productType,
                country_code: params.idInfo?.country || 'NG',
                mock_data: {
                    verification_status: 'VERIFIED',
                    confidence_score: 0.95,
                    processing_time_ms: 1500
                }
            }
        };

        if (__DEV__) {
            console.log(`SmileID Mock: ${productType} simulation completed`, mockResult);
        }

        onResult?.(mockResult);
    };

    const handleMockError = () => {
        const mockError = {
            error: 'MOCK_ERROR',
            message: 'This is a simulated error for testing purposes',
            code: 'MOCK_SIMULATION'
        };

        if (__DEV__) {
            console.log(`SmileID Mock: ${productType} simulated error`, mockError);
        }

        onError?.(mockError);
    };

    const getProductTitle = () => {
        switch (productType) {
            case 'biometric-kyc':
                return 'Biometric KYC';
            case 'document-verification':
                return 'Document Verification';
            case 'smart-selfie':
                return 'Smart Selfie';
            default:
                return 'Smile ID';
        }
    };

    return (
        <View style={[styles.container, style]}>
            <View style={styles.header}>
                <Text style={styles.title}>Smile ID Mock Mode</Text>
                <Text style={styles.subtitle}>{getProductTitle()}</Text>
            </View>

            {isExpoGo() && (
                <View style={styles.warning}>
                    <Text style={styles.warningText}>
                        Running in Expo Go - Native functionality unavailable
                    </Text>
                    <Text style={styles.warningSubtext}>
                        Use development build for full functionality
                    </Text>
                </View>
            )}

            <View style={styles.content}>
                <Text style={styles.description}>
                    This is a mock implementation for development and testing.
                </Text>
                
                <View style={styles.params}>
                    <Text style={styles.paramsTitle}>Current Parameters:</Text>
                    <Text style={styles.paramText}>User ID: {params.userId || 'N/A'}</Text>
                    <Text style={styles.paramText}>Job ID: {params.jobId || 'N/A'}</Text>
                    <Text style={styles.paramText}>Country: {params.idInfo?.country || 'N/A'}</Text>
                </View>

                <View style={styles.actions}>
                    <TouchableOpacity 
                        style={[styles.button, styles.successButton]} 
                        onPress={handleMockResult}
                    >
                        <Text style={styles.buttonText}>Simulate Success</Text>
                    </TouchableOpacity>

                    <TouchableOpacity 
                        style={[styles.button, styles.errorButton]} 
                        onPress={handleMockError}
                    >
                        <Text style={styles.buttonText}>Simulate Error</Text>
                    </TouchableOpacity>
                </View>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        backgroundColor: '#f8f9fa',
        borderRadius: 12,
        padding: 20,
        margin: 10,
        borderWidth: 2,
        borderColor: '#e9ecef',
    },
    header: {
        alignItems: 'center',
        marginBottom: 20,
    },
    title: {
        fontSize: 20,
        fontWeight: 'bold',
        color: '#495057',
        marginBottom: 4,
    },
    subtitle: {
        fontSize: 16,
        color: '#6c757d',
    },
    warning: {
        backgroundColor: '#fff3cd',
        borderColor: '#ffeaa7',
        borderWidth: 1,
        borderRadius: 8,
        padding: 12,
        marginBottom: 20,
    },
    warningText: {
        color: '#856404',
        fontSize: 14,
        fontWeight: '600',
        textAlign: 'center',
    },
    warningSubtext: {
        color: '#856404',
        fontSize: 12,
        textAlign: 'center',
        marginTop: 4,
    },
    content: {
        flex: 1,
    },
    description: {
        fontSize: 14,
        color: '#6c757d',
        textAlign: 'center',
        marginBottom: 20,
        lineHeight: 20,
    },
    params: {
        backgroundColor: '#ffffff',
        borderRadius: 8,
        padding: 12,
        marginBottom: 20,
        borderWidth: 1,
        borderColor: '#dee2e6',
    },
    paramsTitle: {
        fontSize: 14,
        fontWeight: '600',
        color: '#495057',
        marginBottom: 8,
    },
    paramText: {
        fontSize: 12,
        color: '#6c757d',
        marginBottom: 2,
    },
    actions: {
        gap: 12,
    },
    button: {
        padding: 16,
        borderRadius: 8,
        alignItems: 'center',
    },
    successButton: {
        backgroundColor: '#28a745',
    },
    errorButton: {
        backgroundColor: '#dc3545',
    },
    buttonText: {
        color: '#ffffff',
        fontSize: 16,
        fontWeight: '600',
    },
});
