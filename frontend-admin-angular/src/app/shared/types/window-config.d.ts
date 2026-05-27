declare global {
  interface Window {
    __PASTELERIA_CONFIG__?: {
      apiBaseUrl?: string;
      appName?: string;
    };
  }
}

export {};
