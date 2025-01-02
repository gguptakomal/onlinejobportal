CREATE DATABASE online_job_portal;
USE online_job_portal;
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role ENUM('Admin', 'Employer', 'JobSeeker') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE JobListings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employer_id INT NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    job_description TEXT NOT NULL,
    location VARCHAR(100) NOT NULL,
    salary DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employer_id) REFERENCES Users(id)
);
CREATE TABLE Applications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    job_id INT NOT NULL,
    seeker_id INT NOT NULL,
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('Pending', 'Accepted', 'Rejected') DEFAULT 'Pending',
    FOREIGN KEY (job_id) REFERENCES JobListings(id),
    FOREIGN KEY (seeker_id) REFERENCES Users(id)
);
-- Insert Users
INSERT INTO Users (username, password, email, role) VALUES
('admin', 'adminpassword', 'admin@jobportal.com', 'Admin'),
('employer1', 'employerpassword', 'employer1@jobportal.com', 'Employer'),
('jobseeker1', 'seekerpassword', 'seeker1@jobportal.com', 'JobSeeker');

-- Insert Job Listings
INSERT INTO JobListings (employer_id, job_title, job_description, location, salary) VALUES
(2, 'Software Engineer', 'Develop and maintain web applications.', 'New York', 75000.00),
(2, 'Data Scientist', 'Analyze and interpret complex data.', 'San Francisco', 95000.00);

-- Insert Applications
INSERT INTO Applications (job_id, seeker_id, status) VALUES
(1, 3, 'Pending');
-- Index on job title for faster searches
CREATE INDEX idx_job_title ON JobListings (job_title);

-- Index on seeker_id for faster application lookups
CREATE INDEX idx_seeker_id ON Applications (seeker_id);
