// Main JavaScript file for Consent Management System

// Initialize Alpine.js components
document.addEventListener('alpine:init', () => {
    // Global Alpine data/methods can be defined here
});

// DOM Content Loaded
document.addEventListener('DOMContentLoaded', () => {
    console.log('Consent Management System initialized');
    
    // Initialize any custom components or libraries here
});

// Export functions for use in templates if needed
window.ConsentApp = {
    // Toast notification
    showToast: (message, type = 'info') => {
        if (typeof showToast === 'function') {
            showToast(message, type);
        }
    },
    
    // Other utility functions can be added here
};