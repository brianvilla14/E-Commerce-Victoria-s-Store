<template>
  <RouterView />
  <CartModal />
</template>

<script setup>
import { RouterView } from 'vue-router'
import { useDarkMode } from './composables/useDarkMode'
import CartModal from './components/CartModal.vue'

// Inicializar el tema al montar la app
useDarkMode()
</script>

<style>
:root {
  /* Light mode */
  --bg-primary: #ffffff;
  --bg-secondary: #f9f9f9;
  --bg-tertiary: #f5f5f5;
  --text-primary: #333333;
  --text-secondary: #666666;
  --text-tertiary: #999999;
  --border-color: #e0e0e0;
  --header-bg: linear-gradient(135deg, #fff9f7 0%, #f5f9fb 100%);
  --card-bg: #ffffff;
}

[data-theme="dark"] {
  /* Dark mode */
  --bg-primary: #0a0a0a;
  --bg-secondary: #1a1a1a;
  --bg-tertiary: #2d2d2d;
  --text-primary: #f5f5f5;
  --text-secondary: #c0c0c0;
  --text-tertiary: #888888;
  --border-color: #333333;
  --header-bg: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  --card-bg: #1a1a1a;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  color: var(--text-primary);
  line-height: 1.6;
  background-color: var(--bg-primary);
  transition: background-color 0.3s ease, color 0.3s ease;
}

html, body, #app {
  width: 100%;
  height: 100%;
}
</style>
