module.exports = {
  devServer: {
    port: 8016,
    proxy: {
      '/api': {
        target: 'http://localhost:8015',
        changeOrigin: true
      }
    }
  }
}
