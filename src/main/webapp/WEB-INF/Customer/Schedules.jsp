<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Date & Time Selection</title>
    <link href="default.css" rel="stylesheet">
    <link href="stylepage.css" rel="stylesheet">
    
</head>
<body>
    <header>
        <div class="header">
            <div class="header-container">
                <h1>Select Date & Time</h1>
            </div>
        </div>
    </header>
    <main>
        <div class="date-selector-container" id="date-container">
        </div>
        <section class="time-selection-row">
            <div class="cinema-card">
                <h2>Cinema</h2>
                <div class="showtimes">
                    <button>Time1</button>
                </div>
            </div>
        </section>

    </main>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const container = document.getElementById('date-container');
            const today = new Date();
            
            // Create 7 date buttons (for a week view)
            for (let i = 0; i < 7; i++) {
                const date = new Date();
                date.setDate(today.getDate() + i);
                
                const wrapper = document.createElement('div');
                wrapper.className = 'date-button-wrapper';
                
                const button = document.createElement('button');
                button.className = 'date-button';
                if (i === 0) button.classList.add('active');
                
                const dayName = document.createElement('div');
                dayName.className = 'day-name';
                dayName.textContent = date.toLocaleDateString('en-US', { weekday: 'short' });
                
                const dateNumber = document.createElement('div');
                dateNumber.className = 'date-number';
                dateNumber.textContent = date.getDate();
                
                const monthName = document.createElement('div');
                monthName.className = 'month-name';
                monthName.textContent = date.toLocaleDateString('en-US', { month: 'short' });
                
                const dateInput = document.createElement('input');
                dateInput.type = 'date';
                dateInput.valueAsDate = date;
                
                button.appendChild(dayName);
                button.appendChild(dateNumber);
                button.appendChild(monthName);
                
                button.addEventListener('click', function() {
                    // Remove active class from all buttons
                    document.querySelectorAll('.date-button').forEach(btn => {
                        btn.classList.remove('active');
                    });
                    // Add active class to clicked button
                    this.classList.add('active');
                    // You can add additional logic here for when a date is selected
                    console.log('Selected date:', dateInput.valueAsDate);
                });
                
                wrapper.appendChild(button);
                wrapper.appendChild(dateInput);
                container.appendChild(wrapper);
            }
        });
    </script>
    <footer>
        <div class="footer-container">
            <p>&copy; i like foot</p>
        </div>
    </footer>
</body>
</html>

