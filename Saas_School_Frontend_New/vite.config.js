import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server:{
    open:true,
    port:5173,
    host:true,
    allowedHosts: [
      '.ngrok-free.dev',
      '.ngrok-free.app',
    ],
    proxy: {
      '/service1': {
        target: 'http://localhost:8085',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/service1/, ''),
      },
      '/service2': {
        target: 'http://localhost:8089',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/service2/, ''),
      },
      '/service3': {
        target: 'http://localhost:8090',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/service3/, ''),
      },
      '/service4': {
        target: 'http://localhost:8092',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/service4/, ''),
      },
      '/service5': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/service5/, ''),
      },
      '/service6': {
        target: 'http://localhost:8094',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/service6/, ''),
      },
      '/service7': {
        target: 'http://localhost:8084',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/service7/, ''),
      },
      '/service8': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/service8/, ''),
      },
      '/service9': {
        target: 'http://localhost:8099',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/service9/, ''),
      },
    },
  },
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  define: {
    // Add this to your existing define object or create it if it doesn't exist
    global: 'window',
  },
})
