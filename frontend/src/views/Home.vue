<template>
  <div class="app">
    <Header />
    <Hero />
    <FeaturedProducts />
    <Newsletter />
    <Footer />
  </div>
</template>

<script setup>
import Header from '../components/Header.vue'
import Hero from '../components/Hero.vue'
import FeaturedProducts from '../components/FeaturedProducts.vue'
import Newsletter from '../components/Newsletter.vue'
import Footer from '../components/Footer.vue'
</script>

<style scoped>
.app {
  width: 100%;
  overflow-x: hidden;
}
</style>
