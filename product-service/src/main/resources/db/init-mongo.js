// // Create a new database and switch to it
// db = db.getSiblingDB("product-service");

// Create a user with read and write privileges for the database
db.createUser({
  user: "myuser",
  pwd: "mypassword",
  roles: [{ role: "readWrite", db: "product-service" }],
});
