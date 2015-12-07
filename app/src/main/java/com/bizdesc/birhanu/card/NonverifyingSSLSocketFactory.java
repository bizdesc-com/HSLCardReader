package com.bizdesc.birhanu.card;

/**
 * Created by birhanu on 12/7/15.
 */
class NonverifyingSSLSocketFactory extends SSLSocketFactory {
  SSLContext sslContext = SSLContext.getInstance("TLS");

  public NonverifyingSSLSocketFactory(KeyStore truststore) throws Exception {
    super(truststore);

    TrustManager tm = new X509TrustManager() {
      public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      }

      public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      }

      public X509Certificate[] getAcceptedIssuers() {
        return null;
      }

    };
    sslContext.init(null, new TrustManager[]{tm}, null);
  }

  @Override
  public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
      UnknownHostException {
    return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
  }

  @Override
  public Socket createSocket() throws IOException {
    return sslContext.getSocketFactory().createSocket();
  }

  public static AbstractHttpClient createNonverifyingHttpClient() throws Exception {

    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    trustStore.load(null, null);

    SSLSocketFactory sf = new NonverifyingSSLSocketFactory(trustStore);
    sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    registry.register(new Scheme("https", sf, 443));

    ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

    return new DefaultHttpClient(ccm, params);
  }
}
