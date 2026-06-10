<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-section">
        <h3>Victoria's Store</h3>
        <p>Donde la elegancia y el estilo se encuentran. Explora nuestras colecciones exclusivas.</p>
        <div class="social-links">
          <a href="#" aria-label="Facebook">f</a>
          <a href="#" aria-label="Instagram">📷</a>
          <a href="#" aria-label="Twitter">𝕏</a>
        </div>
      </div>

      <div class="footer-section">
        <h4>Compras</h4>
        <ul>
          <li><RouterLink to="/">Nuevos Productos</RouterLink></li>
          <li><a href="#">Ofertas</a></li>
          <li><a href="#">Bestsellers</a></li>
          <li><a href="#">Colecciones</a></li>
        </ul>
      </div>

      <div class="footer-section">
        <h4>Información</h4>
        <ul>
          <li><a href="#">Sobre Nosotros</a></li>
          <li><a href="#">Contacto</a></li>
          <li><a href="#">Envíos</a></li>
          <li><a href="#">Devoluciones</a></li>
        </ul>
      </div>

      <div class="footer-section">
        <h4>Legales</h4>
        <ul>
          <li><a href="#">Términos y Condiciones</a></li>
          <li><a href="#">Política de Privacidad</a></li>
          <li><a href="#">Cookies</a></li>
          <li><RouterLink to="/login">Admin</RouterLink></li>
        </ul>
      </div>
    </div>

    <div class="footer-bottom">
      <p>&copy; 2026 Victoria's Store. Todos los derechos reservados.</p>
      <p>Diseñado con elegancia para ti</p>
    </div>
  </footer>
</template>

<script setup>
import { RouterLink } from 'vue-router'
</script>

<style scoped>
.footer {
  background: linear-gradient(135deg, var(--bg-secondary) 0%, var(--bg-tertiary) 50%, #0a0a0a 100%);
  color: var(--text-secondary);
  padding: 4rem 2rem 2rem;
  border-top: 2px solid;
  border-image: linear-gradient(90deg, #ff6b9d, #00bcd4) 1;
  transition: background 0.3s ease, color 0.3s ease;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 3rem;
  margin-bottom: 3rem;
}

.footer-section h3 {
  background: linear-gradient(135deg, #ff6b9d 0%, #00bcd4 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

.footer-section h4 {
  color: var(--text-primary);
  margin-bottom: 1rem;
  font-size: 1rem;
  transition: color 0.3s ease;
}

.footer-section p {
  color: var(--text-secondary);
  font-size: 0.9rem;
  line-height: 1.6;
  margin-bottom: 1rem;
  transition: color 0.3s ease;
}

.social-links {
  display: flex;
  gap: 1rem;
  margin-top: 1.5rem;
}

.social-links a {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: rgba(255, 107, 157, 0.2);
  border-radius: 50%;
  color: #ff6b9d;
  text-decoration: none;
  transition: all 0.3s ease;
  font-size: 1rem;
}

.social-links a:hover {
  background: #ff6b9d;
  color: white;
  transform: translateY(-3px);
}

.footer-section ul {
  list-style: none;
}

.footer-section ul li {
  margin-bottom: 0.8rem;
}

.footer-section a {
  color: #aaa;
  text-decoration: none;
  transition: all 0.3s ease;
  font-size: 0.9rem;
}

.footer-section a:hover {
  color: #ff6b9d;
  padding-left: 5px;
}

.footer-bottom {
  border-top: 1px solid rgba(255, 107, 157, 0.2);
  padding-top: 2rem;
  text-align: center;
  color: #888;
  font-size: 0.85rem;
}

.footer-bottom p:first-child {
  margin-bottom: 0.5rem;
}

@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
    gap: 2rem;
  }

  .footer-section h3 {
    font-size: 1.2rem;
  }
}
</style>
