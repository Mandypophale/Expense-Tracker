/**
 * Expense Tracker Application Scripts
 */
document.addEventListener("DOMContentLoaded", function () {
    // Sidebar toggle for mobile and tablet screens
    const toggleBtn = document.getElementById("sidebarToggle");
    const sidebar = document.querySelector(".sidebar");
    
    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener("click", function (e) {
            e.stopPropagation();
            sidebar.classList.toggle("show");
        });

        // Close sidebar when clicking outside on mobile
        document.addEventListener("click", function (e) {
            if (window.innerWidth < 992 && sidebar.classList.contains("show")) {
                if (!sidebar.contains(e.target) && e.target !== toggleBtn) {
                    sidebar.classList.remove("show");
                }
            }
        });
    }

    // Auto-close alert notifications after 4 seconds
    const alerts = document.querySelectorAll(".alert-dismissible");
    alerts.forEach(function (alert) {
        setTimeout(function () {
            if (typeof bootstrap !== 'undefined' && bootstrap.Alert) {
                const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
                if (bsAlert) {
                    bsAlert.close();
                }
            } else {
                // Fallback if Bootstrap is not loaded fully
                alert.style.transition = "opacity 0.5s ease";
                alert.style.opacity = "0";
                setTimeout(() => alert.remove(), 500);
            }
        }, 4000);
    });
});

/**
 * Standard confirmation popup before executing destructive actions (delete).
 * @param {Event} event - DOM event
 * @param {String} message - Custom prompt message
 */
function confirmDelete(event, message) {
    const defaultMsg = "Are you sure you want to delete this record? This action cannot be undone.";
    const confirmed = confirm(message || defaultMsg);
    if (!confirmed) {
        event.preventDefault();
        return false;
    }
    return true;
}
