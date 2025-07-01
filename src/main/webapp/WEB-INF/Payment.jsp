<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Payment</title>
    <link href="default.css" rel="stylesheet">
    <link href="stylepage.css" rel="stylesheet">
</head>
<body>
    <header>
        <div class="header">
            <div class="header-container">
                <h1>Payment</h1>
            </div>
        </div>
    </header>
    <main>
        <div class="payment-form">
            <h2>Enter Card Details</h2>
            <form method="post">
                <input type="hidden" name="id" value="<%=request.getParameter("id")%>">

                <div class="form-group">
                    <label for="cardNumber">Card Number</label>
                    <input type="text" id="cardNumber" name="cardNumber" placeholder="1234 5678 9012 3456" required>
                </div>

                <div class="form-group">
                    <label for="nameOnCard">Name on Card</label>
                    <input type="text" id="nameOnCard" name="nameOnCard" placeholder="Lee Guang En" required>
                </div>

                <div style="display: flex; gap: 15px;">
                    <div class="form-group" style="flex: 1;">
                        <label for="expiryDate">Expiry Date</label>
                        <input type="text" id="expiryDate" name="expiryDate" placeholder="MM/YY" required>
                    </div>

                    <div class="form-group" style="flex: 1;">
                        <label for="cvv">CVV</label>
                        <input type="text" id="cvv" name="cvv" placeholder="123" required>
                    </div>
                </div>

                <div class="card-icons">
                    <i class="fab fa-cc-visa" style="font-size: 30px; color: #1a1f71;"></i>
                    <i class="fab fa-cc-mastercard" style="font-size: 30px; color: #eb001b;"></i>
                </div>

                <button type="submit" class="submit-btn">Complete Payment</button>
            </form>
        </div>
    </main>
    <footer>
        <div class="footer-container">
            <p>&copy; i like foot</p>
        </div>
    </footer>
</body>
</html>
