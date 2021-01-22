let baseURL = "http://localhost:12306";
module.exports = {
  proxyList: {
    '/': {
      target: baseURL,
      changeOrigin: true,
      pathRewrite: {
        '^/':''
      }
    }
  }
};
