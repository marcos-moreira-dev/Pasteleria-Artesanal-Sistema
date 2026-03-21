const runtimeConfig = window.__PASTELERIA_CONFIG__ ?? {};

export const apiConfig = {
  baseUrl: runtimeConfig.apiBaseUrl ?? "http://localhost:8081/api/v1"
};
