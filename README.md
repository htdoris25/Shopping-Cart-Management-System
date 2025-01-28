# Shopping Cart Management System
<img width="777" alt="MainCart" src="https://github.com/user-attachments/assets/fe081923-b83c-4334-93a1-6d51b2a5a727" />  

The **JavaFX Login, Registration, and Shopping Cart Management System** is a user-friendly desktop application integrating secure authentication and shopping cart management. Users can log in with credentials validated against an SQLite database or register for a new account. The shopping cart feature allows users to add, remove, and save items, view saved carts, and perform checkouts with confirmation prompts. After checkout, selected carts are automatically deleted. The application provides interactive alerts for success and error messages, ensuring a smooth user experience. Built with JavaFX and following the MVC architecture, it offers clean, modular, and maintainable code for robust functionality.# Features

## Technologies Used
- **JavaFX**: For creating the graphical user interface.
- **SQLite**: To manage cart and item data persistently.
- **Gson**: For serializing and deserializing cart data into JSON format.
- **Maven**: For dependency management and building the project.

## Login / Register
- LoginController, RegisterController, login.fxml, register.fxml
- Validates credentials using validateLogin method.
Transitions to the shopping cart window on successful login.
<img width="774" alt="Login Register" src="https://github.com/user-attachments/assets/db8404d0-3194-4cb4-b8b2-d66c55d79672" />

## Shopping Cart
- Add Items: Add items to the cart from a predefined list with adjustable quantities.
- Remove Items: Remove selected items from the cart.
- View Total: Automatically calculates and displays the total price of items in the cart.
  
<img width="611" alt="MainShoppingCart" src="https://github.com/user-attachments/assets/5d62962c-ff46-483f-9e75-a974bddaf138" />

## Saved Carts
- Save Cart: Save the current cart for future use.
- View Saved Carts: Display a list of previously saved carts.
  
  <img width="956" alt="ViewSaveCart " src="https://github.com/user-attachments/assets/73d4bc14-e502-4581-abb5-ae16f24b5f2a" />

- Checkout with Confirmation: Before proceeding to checkout, users are prompted with a confirmation dialog.
  
  <img width="739" alt=" CheckOut" src="https://github.com/user-attachments/assets/b5e44d7f-5ffc-4a43-9e27-b4342a4ab7be" />

- Automatic Deletion: After a successful checkout, the selected carts are automatically deleted from the system.
  <img width="421" alt="CheckOutSucess" src="https://github.com/user-attachments/assets/9d263ea4-aeb6-4b3e-be49-d240aeba74a0" />

  


