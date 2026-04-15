import React, {useEffect, useState} from 'react';
import {Alert, FlatList, SafeAreaView, StyleSheet, Text, TouchableOpacity, View} from 'react-native';

import {
  AutoCapture,
  BiometricKYCParams,
  ConsentInformationParams,
  DocumentVerificationParams,
  EnhancedDocumentVerificationParams,
  IdInfoParams,
  initialize,
  SmartSelfieParams,
  SmileConfig,
  SmileIDBiometricKYCView,
  SmileIDDocumentVerificationEnhancedView,
  SmileIDDocumentVerificationView,
  SmileIDSmartSelfieAuthenticationEnhancedView,
  SmileIDSmartSelfieAuthenticationView,
  SmileIDSmartSelfieEnrollmentEnhancedView,
  SmileIDSmartSelfieEnrollmentView,
  SmileSensitivity,
} from '@smile_identity/react-native-expo';

import DocumentVerificationEnhancedSvgIcon from './icons/DocumentVerificationEnhancedSvgIcon';
import BiometricKYCSvgIcon from './icons/BiometricKYCSvgIcon';
import DocumentVerificationSvgIcon from './icons/DocumentVerificationSvgIcon';
import SmartSelfieEnrollmentSvgIcon from './icons/SmartSelfieEnrollmentSvgIcon';
import SmartSelfieAuthenticationSvgIcon from './icons/SmartSelfieAuthenticationSvgIcon';

const PRODUCTS = [
  {
    title: 'SmartSelfie™ Enrollment',
    key: 'smartSelfieEnrollment',
    icon: <SmartSelfieEnrollmentSvgIcon width={48} height={48} />,
  },
  {
    title: 'SmartSelfie™ Authentication',
    key: 'smartSelfieAuth',
    icon: <SmartSelfieAuthenticationSvgIcon width={48} height={48} />,
  },
  {
    title: 'SmartSelfie™ Enrollment (Enhanced)',
    key: 'smartSelfieEnrollmentEnhanced',
    icon: <SmartSelfieEnrollmentSvgIcon width={48} height={48} />,
  },
  {
    title: 'SmartSelfie™ Authentication (Enhanced)',
    key: 'smartSelfieAuthEnhanced',
    icon: <SmartSelfieAuthenticationSvgIcon width={48} height={48} />,
  },
  {
    title: 'Biometric KYC',
    key: 'biometricKYC',
    icon: <BiometricKYCSvgIcon width={48} height={48} />,
  },
  {
    title: 'Document Verification',
    key: 'documentVerification',
    icon: <DocumentVerificationSvgIcon width={48} height={48} />,
  },
  {
    title: 'Enhanced Document Verification',
    key: 'enhancedDocumentVerification',
    icon: <DocumentVerificationEnhancedSvgIcon width={48} height={48} />,
  },
];

const config = new SmileConfig(
  'your_partner_id', // partnerId
  'your_auth_token', // authToken
  'https://prod-lambda-url.com', // prodLambdaUrl
  'https://test-lambda-url.com' // testLambdaUrl
);

const jobId = 'your_job_id'; // Replace with your actual job ID
const callbackUrl = 'https://your-callback-url.com'; // Replace with your actual callback URL

const documentVerificationParams: DocumentVerificationParams = {
  // userId: 'user123', // Optional user ID
  // jobId: 'job456', // Optional job ID
  countryCode: 'NG',
  allowNewEnroll: false,
  documentType: 'PASSPORT',
  // idAspectRatio: 1.0, // Optional aspect ratio for document capture
  // bypassSelfieCaptureWithFile: '', // Optional file path to bypass selfie capture
  autoCaptureTimeout: 10, // this is in seconds,
  autoCapture: AutoCapture.AutoCapture,
  captureBothSides: false,
  allowAgentMode: false,
  forceAgentMode: false,
  showInstructions: true,
  showAttribution: true,
  allowGalleryUpload: true,
  skipApiSubmission: false,
  smileSensitivity: SmileSensitivity.Normal,
  useStrictMode: false,
  extraPartnerParams: {
    'custom_param_1': 'value1',
    'custom_param_2': 'value2',
  },
};

const smartSelfieParams: SmartSelfieParams = {
  // userId: 'user123', // Optional user ID
  // jobId: 'job456', // Optional job ID
  allowNewEnroll: false,
  allowAgentMode: false,
  forceAgentMode: false,
  showAttribution: true,
  showInstructions: true,
  skipApiSubmission: false,
  useStrictMode: false,
  smileSensitivity: SmileSensitivity.Normal,
  extraPartnerParams: {
    'custom_param_1': 'value1',
    'custom_param_2': 'value2',
  },
};

const consentInformationParams: ConsentInformationParams = {
  consentGrantedDate: '2025-07-25T09:20:25.362Z',
  personalDetails: true,
  contactInformation: true,
  documentInformation: true,
};

const enhancedDocumentVerificationParams: EnhancedDocumentVerificationParams = {
  // userId: 'user123', // Optional user ID
  // jobId: 'job456', // Optional job ID
  countryCode: 'NG',
  allowNewEnroll: false,
  documentType: 'PASSPORT',
  // idAspectRatio: 1.0, // Optional aspect ratio for document capture
  // bypassSelfieCaptureWithFile: '', // Optional file path to bypass selfie capture
  autoCaptureTimeout: 10, // this is in seconds,
  autoCapture: AutoCapture.AutoCapture,
  captureBothSides: false,
  allowGalleryUpload: true,
  showInstructions: true,
  showAttribution: true,
  skipApiSubmission: false,
  useStrictMode: false,
  extraPartnerParams: {
    'custom_param_1': 'value1',
    'custom_param_2': 'value2',
  },
  consentInformation: consentInformationParams, // Optional consent information
};


const idInfoParams: IdInfoParams = {
  country: 'NG',
  idType: 'NIN_V2',
  idNumber: '00000000000',
  firstName: 'John',
  middleName: 'A',
  lastName: 'Doe',
  dob: '1990-01-01',
  bankCode: '1234567890',
  entered: false,
};

const biometricKYCParams: BiometricKYCParams = {
  // userId: 'user123', // Optional user ID
  // jobId: 'job456', // Optional job ID
  allowNewEnroll: false,
  allowAgentMode: false,
  forceAgentMode: false,
  showAttribution: true,
  showInstructions: true,
  smileSensitivity: SmileSensitivity.Normal,
  skipApiSubmission: false,
  useStrictMode: false,
  extraPartnerParams: {
    'custom_param_1': 'value1',
    'custom_param_2': 'value2',
  },
  consentInformation: consentInformationParams, // Optional consent information
  idInfo: idInfoParams,
};


export default function HomeScreen() {
  const [selectedProduct, setSelectedProduct] = useState<string | null>(null);

  useEffect(() => {
    const setupSmileID = async () => {
      try {
        await initialize(true, true, config, undefined);
        console.log('[SmileID] SDK initialized');

        // Set the callback URL if needed
        // await setCallbackUrl(callbackUrl);
        // console.log('[SmileID] Callback URL set');

        // Enable offline mode if needed
        // await setAllowOfflineMode(true);
        // console.log('[SmileID] Offline mode enabled');

      } catch (error) {
        console.error('[SmileID] Setup failed:', error);
        Alert.alert('SmileID Setup Error', 'Failed to initialize SDK');
      }
    };

    setupSmileID();
  }, []);

  useEffect(() => {
    const handleJobs = async () => {
      try {
        // Submit a job if needed
        // await submitJob(jobId);
        // console.log('[SmileID] Job submitted');

        // Get submitted jobs if needed
        // const submitted = await getSubmittedJobs();
        // console.log('[SmileID] Submitted jobs:', submitted);

        // Get unsubmitted jobs if needed
        // const unsubmitted = await getUnsubmittedJobs();
        // console.log('[SmileID] Unsubmitted jobs:', unsubmitted);

        // Clean up a job if needed
        // await cleanup(jobId);
        console.log('[SmileID] Job cleaned up');
      } catch (error) {
        console.error('[SmileID] Job operation failed:', error);
      }
    };
    handleJobs();
  }, []);

  const handleProductPress = (productKey: string) => {
    setSelectedProduct(productKey);
  };

  const handleSuccessResult = (event: any) => {
    console.log('Success result:', event.nativeEvent);
    setSelectedProduct(null);
  };

  const handleError = (event: any) => {
    console.log('Got error:', event.nativeEvent);
    Alert.alert('Got error', event.nativeEvent?.error || 'Something went wrong');
    setSelectedProduct(null);
  };

  const renderSelectedProductView = () => {
    switch (selectedProduct) {
      case 'documentVerification':
        return (
          <View style={styles.nativeContainer}>
            <SmileIDDocumentVerificationView
              style={styles.nativeView}
              params={documentVerificationParams}
              onResult={handleSuccessResult}
              onError={handleError}
            />
          </View>
        );
      case 'enhancedDocumentVerification':
        return (
          <View style={styles.nativeContainer}>
            <SmileIDDocumentVerificationEnhancedView
              style={styles.nativeView}
              params={enhancedDocumentVerificationParams}
              onResult={handleSuccessResult}
              onError={handleError}
            />
          </View>
        );
      case 'smartSelfieEnrollment':
        return (
          <View style={styles.nativeContainer}>
            <SmileIDSmartSelfieEnrollmentView
              style={styles.nativeView}
              params={smartSelfieParams}
              onResult={handleSuccessResult}
              onError={handleError}/>
          </View>
        );
      case 'smartSelfieEnrollmentEnhanced':
        return (
          <View style={styles.nativeContainer}>
            <SmileIDSmartSelfieEnrollmentEnhancedView
              style={styles.nativeView}
              params={smartSelfieParams}
              onResult={handleSuccessResult}
              onError={handleError}
            />
          </View>
        );
      case 'smartSelfieAuth':
        return (
          <View style={styles.nativeContainer}>
            <SmileIDSmartSelfieAuthenticationView
              style={styles.nativeView}
              params={smartSelfieParams}
              onResult={handleSuccessResult}
              onError={handleError}
            />
          </View>
        );
      case 'smartSelfieAuthEnhanced':
        return (
          <View style={styles.nativeContainer}>
            <SmileIDSmartSelfieAuthenticationEnhancedView
              style={styles.nativeView}
              params={smartSelfieParams}
              onResult={handleSuccessResult}
              onError={handleError}
            />
          </View>
        );
      case 'biometricKYC':
        return (
          <View style={styles.nativeContainer}>
            <SmileIDBiometricKYCView
              style={styles.nativeView}
              params={biometricKYCParams}
              onResult={handleSuccessResult}
              onError={handleError}
            />
          </View>
        );
      default:
        return null;
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      {selectedProduct ? (
        renderSelectedProductView()
      ) : (
        <>
          <View style={styles.headerBar}>
            <Text style={styles.appTitle}>Smile ID</Text>
          </View>

          <FlatList
            data={PRODUCTS}
            keyExtractor={(item) => item.key}
            numColumns={2}
            contentContainerStyle={styles.content}
            columnWrapperStyle={styles.gridRow}
            ListHeaderComponent={() => (
              <Text style={styles.sectionTitle}>Test Our Products</Text>
            )}
            ListFooterComponent={() => (
              <Text style={styles.version}>
                Partner O05 • 1.6_11.0.4-SNAPSHOT_debug
              </Text>
            )}
            renderItem={({ item }) => (
              <TouchableOpacity
                style={styles.card}
                onPress={() => handleProductPress(item.key)}
              >
                <View style={styles.icon}>{item.icon}</View>
                <Text style={styles.cardTitle}>{item.title}</Text>
              </TouchableOpacity>
            )}
          />
        </>
      )}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#dedfc6',
  },
  headerBar: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingVertical: 12,
  },
  appTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#111',
  },
  content: {
    paddingHorizontal: 16,
    paddingBottom: 40,
  },
  sectionTitle: {
    fontSize: 22,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 16,
    color: '#222',
  },
  gridRow: {
    justifyContent: 'space-between',
    marginBottom: 16,
  },
  card: {
    backgroundColor: '#1b2471',
    width: '47%',
    borderRadius: 20,
    padding: 16,
    alignItems: 'center',
  },
  icon: {
    width: 48,
    height: 48,
    marginBottom: 12,
    overflow: 'hidden',
    alignItems: 'center',
    justifyContent: 'center',
  },
  cardTitle: {
    color: '#fff',
    fontSize: 14,
    textAlign: 'center',
    fontWeight: '600',
  },
  nativeContainer: {
    flex: 1,
    backgroundColor: '#fff',
  },
  nativeView: {
    flex: 1,
    width: '100%',
  },
  version: {
    textAlign: 'center',
    marginTop: 16,
    color: '#555',
    fontSize: 12,
  },
});
