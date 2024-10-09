#Dette
#1. Users (Brukere)
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    phone_number VARCHAR(15),
    address VARCHAR(150),
    role ENUM('Eier', 'Beboer', 'Gjest'),
    date_registered DATETIME
);

#2. Owners (Eiere)
CREATE TABLE Owners (
    owner_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    hub_id INT,
    purchase_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

#3. Hubs (HUBer)
CREATE TABLE Hubs (
    hub_id INT AUTO_INCREMENT PRIMARY KEY,
    hub_name VARCHAR(100),
    owner_id INT,
    installation_date DATETIME,
    status ENUM('Active', 'Inactive'),
    FOREIGN KEY (owner_id) REFERENCES Owners(owner_id)
);

#4. Locks (Låser)
CREATE TABLE Locks (
    lock_id INT AUTO_INCREMENT PRIMARY KEY,
    hub_id INT,
    door_name VARCHAR(50),
    lock_status ENUM('Locked', 'Unlocked'),
    battery_status INT,
    mechanical_status ENUM('OK', 'Fault'),
    FOREIGN KEY (hub_id) REFERENCES Hubs(hub_id)
);

#5. Access_Log (Tilgangslogg)
CREATE TABLE Access_Log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    lock_id INT,
    action ENUM('Locked', 'Unlocked'),
    timestamp DATETIME,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (lock_id) REFERENCES Locks(lock_id)
);

#6. User_Hub_Access (Bruker-HUB-tilgang)
CREATE TABLE User_Hub_Access (
    access_id INT AUTO_INCREMENT PRIMARY KEY,
    hub_id INT,
    user_id INT,
    access_role ENUM('Owner', 'Resident', 'Guest'),
    access_start_time DATETIME,
    access_end_time DATETIME,
    FOREIGN KEY (hub_id) REFERENCES Hubs(hub_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

#7. Temporary_Keys (Midlertidige nøkler)
CREATE TABLE Temporary_Keys (
    key_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    recipient_id INT,
    lock_id INT,
    qr_code VARCHAR(255),
    valid_from DATETIME,
    valid_until DATETIME,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (recipient_id) REFERENCES Users(user_id),
    FOREIGN KEY (lock_id) REFERENCES Locks(lock_id)
);

#8. Error_Log (Feilmeldinger)
CREATE TABLE Error_Log (
    error_id INT AUTO_INCREMENT PRIMARY KEY,
    hub_id INT,
    lock_id INT NULL,
    error_type VARCHAR(255),
    timestamp DATETIME,
    FOREIGN KEY (hub_id) REFERENCES Hubs(hub_id),
    FOREIGN KEY (lock_id) REFERENCES Locks(lock_id)
);
