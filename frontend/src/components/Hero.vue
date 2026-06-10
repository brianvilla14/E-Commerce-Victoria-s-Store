<template>
  <section class="hero">
    <div class="hero-content">
      <h2>Descubre la Elegancia y Estilo</h2>
      <p>Colecciones exclusivas diseñadas para ti</p>
      <button class="cta-btn">Explorar Colección</button>
    </div>
    <div class="hero-image">
      <div class="placeholder-image"></div>
    </div>
  </section>
</template>

<script setup>
</script>

<style scoped>
.hero {
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 50%, var(--bg-tertiary) 100%);
  padding: 6rem 2rem;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  max-width: 1200px;
  margin: 0 auto;
  align-items: center;
  min-height: 500px;
  position: relative;
  overflow: hidden;
  transition: background 0.3s ease;
}

.hero::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -10%;
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(255, 107, 157, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  pointer-events: none;
}

.hero-content h2 {
  font-size: 3rem;
  color: var(--text-primary);
  margin-bottom: 1rem;
  line-height: 1.2;
  background: linear-gradient(135deg, #ff6b9d 0%, #00bcd4 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  transition: color 0.3s ease;
}

.hero-content p {
  font-size: 1.2rem;
  color: var(--text-secondary);
  margin-bottom: 2rem;
  font-weight: 300;
  transition: color 0.3s ease;
}

.cta-btn {
  background: linear-gradient(135deg, #ff6b9d 0%, #e85a8c 100%);
  color: white;
  border: none;
  padding: 1rem 2.5rem;
  font-size: 1rem;
  font-weight: 600;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(255, 107, 157, 0.3);
  text-transform: uppercase;
  letter-spacing: 1px;
  position: relative;
  overflow: hidden;
}

.cta-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.2);
  transition: left 0.4s ease;
}

.cta-btn:hover::before {
  left: 100%;
}

.cta-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(255, 107, 157, 0.5);
}

.hero-image {
  display: flex;
  justify-content: center;
  align-items: center;
}

.placeholder-image {
  width: 100%;
  max-width: 400px;
  aspect-ratio: 1;
  background: linear-gradient(135deg, #f0d4e6 0%, #e6d4f7 50%, #d4f0f7 100%);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3rem;
  color: #00bcd4;
  box-shadow: 0 15px 45px rgba(255, 107, 157, 0.2);
  animation: floatImage 4s ease-in-out infinite;
  border: 2px solid rgba(255, 107, 157, 0.1);
}

@keyframes floatImage {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-15px); }
}

@media (max-width: 768px) {
  .hero {
    grid-template-columns: 1fr;
    padding: 3rem 1rem;
    gap: 2rem;
    min-height: auto;
  }

  .hero-content h2 {
    font-size: 2rem;
  }

  .hero-content p {
    font-size: 1rem;
  }
}
</style>
