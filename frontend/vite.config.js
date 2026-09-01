import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig(() => {
  const backendUrl = process.env.VITE_BACKEND_URL || 'http://localhost:8080'

  return {
    plugins: [vue()],
    server: {
      port: 5173,
      host: true,
      middlewareMode: false,
      proxy: {
        '/api': {
          target: backendUrl,
          changeOrigin: true
        }
      }
    },
    resolve: {
      alias: {
        vue: 'vue/dist/vue.esm-bundler.js'
      }
    }
  }
})
