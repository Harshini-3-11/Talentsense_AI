-- TalentSense AI Database Migration - Extended Candidate Entities
-- Version: V2

CREATE TABLE IF NOT EXISTS candidate_skills (
    id VARCHAR(36) PRIMARY KEY,
    candidate_id VARCHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) DEFAULT 'TECHNICAL', -- TECHNICAL, SOFT, TOOL, DOMAIN
    proficiency VARCHAR(50) DEFAULT 'INTERMEDIATE', -- BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    years_experience INT DEFAULT 1,
    FOREIGN KEY (candidate_id) REFERENCES candidate_profiles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS candidate_experiences (
    id VARCHAR(36) PRIMARY KEY,
    candidate_id VARCHAR(36) NOT NULL,
    company VARCHAR(150) NOT NULL,
    title VARCHAR(150) NOT NULL,
    location VARCHAR(100),
    start_date DATE NOT NULL,
    end_date DATE,
    is_current BOOLEAN DEFAULT FALSE,
    description TEXT,
    FOREIGN KEY (candidate_id) REFERENCES candidate_profiles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS candidate_educations (
    id VARCHAR(36) PRIMARY KEY,
    candidate_id VARCHAR(36) NOT NULL,
    institution VARCHAR(150) NOT NULL,
    degree VARCHAR(100) NOT NULL,
    field_of_study VARCHAR(100),
    start_date DATE,
    end_date DATE,
    gpa VARCHAR(20),
    FOREIGN KEY (candidate_id) REFERENCES candidate_profiles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS candidate_projects (
    id VARCHAR(36) PRIMARY KEY,
    candidate_id VARCHAR(36) NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    technologies VARCHAR(255),
    repo_url VARCHAR(255),
    demo_url VARCHAR(255),
    FOREIGN KEY (candidate_id) REFERENCES candidate_profiles(id) ON DELETE CASCADE
);
