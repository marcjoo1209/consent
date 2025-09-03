// Import dependencies
import Chart from 'chart.js/auto';
import Alpine from 'alpinejs';

// Initialize Alpine.js
window.Alpine = Alpine;
Alpine.start();

// DOM Content Loaded
document.addEventListener("DOMContentLoaded", () => {
  console.log("Consent Management System initialized");
});

// Export functions for use in templates if needed
window.ConsentApp = {
  // Toast notification
  showToast: (message, type = "info") => {
    if (typeof showToast === "function") {
      showToast(message, type);
    }
  },

  // Modal control for component templates
  openModal: (modalId) => {
    const modal = document.getElementById(modalId);
    if (modal) {
      modal.classList.remove("hidden");
    }
  },

  closeModal: (modalId) => {
    const modal = document.getElementById(modalId);
    if (modal) {
      modal.classList.add("hidden");
    }
  },

  // Dashboard chart initialization
  initDashboardCharts: () => {
    // Sales Chart
    const salesCtx = document.getElementById("salesChart");
    if (salesCtx) {
      new Chart(salesCtx.getContext("2d"), {
        type: "line",
        data: {
          labels: ["1월", "2월", "3월", "4월", "5월", "6월"],
          datasets: [
            {
              label: "매출",
              data: [12000, 19000, 15000, 25000, 22000, 30000],
              borderColor: "rgb(59, 130, 246)",
              backgroundColor: "rgba(59, 130, 246, 0.1)",
              tension: 0.4,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false,
            },
          },
        },
      });
    }

    // Traffic Chart (Doughnut)
    const trafficCtx = document.getElementById("trafficChart");
    if (trafficCtx) {
      new Chart(trafficCtx.getContext("2d"), {
        type: "doughnut",
        data: {
          labels: ["직접", "소셜", "추천", "검색"],
          datasets: [
            {
              data: [30, 25, 20, 25],
              backgroundColor: ["rgb(59, 130, 246)", "rgb(34, 197, 94)", "rgb(251, 146, 60)", "rgb(168, 85, 247)"],
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
        },
      });
    }

    // Traffic Overview
    const overviewCtx = document.getElementById("trafficOverview");
    if (overviewCtx) {
      new Chart(overviewCtx.getContext("2d"), {
        type: "bar",
        data: {
          labels: ["월", "화", "수", "목", "금", "토", "일"],
          datasets: [
            {
              label: "방문자",
              data: [2400, 1398, 3800, 3908, 4800, 3800, 4300],
              backgroundColor: "rgba(59, 130, 246, 0.8)",
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false,
            },
          },
        },
      });
    }
  },
};

// Initialize dashboard charts when DOM is loaded
document.addEventListener("DOMContentLoaded", () => {
  // Check if we're on a dashboard page
  if (document.getElementById("salesChart") || document.getElementById("trafficChart") || document.getElementById("trafficOverview")) {
    ConsentApp.initDashboardCharts();
  }

  // Setup modal event listeners
  const modalTriggers = document.querySelectorAll("[data-modal-toggle]");
  modalTriggers.forEach((trigger) => {
    trigger.addEventListener("click", () => {
      const modalId = trigger.getAttribute("data-modal-toggle");
      ConsentApp.openModal(modalId);
    });
  });

  const modalCloses = document.querySelectorAll("[data-modal-close]");
  modalCloses.forEach((close) => {
    close.addEventListener("click", () => {
      const modalId = close.getAttribute("data-modal-close");
      ConsentApp.closeModal(modalId);
    });
  });
});
