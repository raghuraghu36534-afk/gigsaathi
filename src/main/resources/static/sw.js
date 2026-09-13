const CACHE_NAME = 'gigsaathi-v1';
const STATIC_ASSETS = [
  '/manifest.json',
  '/icon-192.png',
  '/icon-512.png',
  '/.well-known/assetlinks.json'
];

// Install event - cache static assets only
self.addEventListener('install', (event) => {
  console.log('[Service Worker] Installing...');
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then((cache) => {
        console.log('[Service Worker] Caching static assets');
        return cache.addAll(STATIC_ASSETS);
      })
      .catch((error) => {
        console.error('[Service Worker] Cache installation failed:', error);
      })
  );
});

// Activate event - clean up old caches
self.addEventListener('activate', (event) => {
  console.log('[Service Worker] Activating...');
  event.waitUntil(
    caches.keys()
      .then((cacheNames) => {
        return Promise.all(
          cacheNames.map((cacheName) => {
            if (cacheName !== CACHE_NAME) {
              console.log('[Service Worker] Deleting old cache:', cacheName);
              return caches.delete(cacheName);
            }
          })
        );
      })
  );
});

// Fetch event - network-first for HTML, cache-only for static assets
self.addEventListener('fetch', (event) => {
  const url = new URL(event.request.url);

  // For HTML pages and API calls, always use network (no caching)
  if (url.pathname.startsWith('/api/') || 
      url.pathname.endsWith('.html') || 
      url.pathname === '/' ||
      url.pathname.startsWith('/dashboard') ||
      url.pathname.startsWith('/schemes') ||
      url.pathname.startsWith('/login') ||
      url.pathname.startsWith('/register') ||
      url.pathname.startsWith('/users') ||
      url.pathname.startsWith('/earning') ||
      url.pathname.startsWith('/.well-known')) {
    
    event.respondWith(
      fetch(event.request)
        .catch((error) => {
          console.error('[Service Worker] Network request failed:', error);
          // Return a basic offline message for HTML pages
          if (event.request.headers.get('accept')?.includes('text/html')) {
            return new Response(
              '<html><body><h1>Offline</h1><p>Please check your internet connection.</p></body></html>',
              { headers: { 'Content-Type': 'text/html' } }
            );
          }
          throw error;
        })
    );
    return;
  }

  // For static assets (manifest, icons), use cache
  event.respondWith(
    caches.match(event.request)
      .then((response) => {
        if (response) {
          console.log('[Service Worker] Serving from cache:', event.request.url);
          return response;
        }
        
        return fetch(event.request)
          .then((response) => {
            // Cache successful responses for static assets
            if (response.ok && STATIC_ASSETS.some(asset => event.request.url.includes(asset))) {
              const responseClone = response.clone();
              caches.open(CACHE_NAME)
                .then((cache) => {
                  cache.put(event.request, responseClone);
                });
            }
            return response;
          })
          .catch((error) => {
            console.error('[Service Worker] Fetch failed:', error);
            throw error;
          });
      })
  );
});
