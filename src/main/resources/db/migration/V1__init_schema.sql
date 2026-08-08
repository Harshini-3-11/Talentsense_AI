-- TalentSense AI Database Initialization Schema
-- Version: V1

CREATE TABLE IF NOT EXISTS roles (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

INSERT INTO roles (id, name, description) VALUES
('r-101', 'ROLE_CANDIDATE', 'Job candidate / applicant'),
('r-102', 'ROLE_RECRUITER', 'Recruiter / talent acquisition officer'),
('r-103', 'ROLE_HIRING_MANAGER', 'Hiring manager / interviewer'),
('r-104', 'ROLE_ADMIN', 'Platform system administrator')
ON DUPLICATE KEY UPDATE name=name;

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    user_type VARCHAR(50) NOT NULL, -- CANDIDATE, RECRUITER, ADMIN
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id VARCHAR(36) NOT NULL,
    role_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS organizations (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    company_domain VARCHAR(100),
    industry VARCHAR(100),
    company_size VARCHAR(50),
    website_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS candidate_profiles (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL UNIQUE,
    headline VARCHAR(255),
    summary TEXT,
    location VARCHAR(100),
    phone VARCHAR(30),
    github_url VARCHAR(255),
    linkedin_url VARCHAR(255),
    portfolio_url VARCHAR(255),
    career_readiness_score INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recruiter_profiles (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL UNIQUE,
    organization_id VARCHAR(36),
    job_title VARCHAR(100),
    department VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS jobs (
    id VARCHAR(36) PRIMARY KEY,
    organization_id VARCHAR(36),
    created_by_user_id VARCHAR(36) NOT NULL,
    title VARCHAR(150) NOT NULL,
    department VARCHAR(100),
    description TEXT NOT NULL,
    location VARCHAR(100),
    remote_type VARCHAR(50), -- REMOTE, HYBRID, ON_SITE
    employment_type VARCHAR(50), -- FULL_TIME, PART_TIME, CONTRACT
    salary_min DECIMAL(12,2),
    salary_max DECIMAL(12,2),
    currency VARCHAR(10) DEFAULT 'USD',
    experience_min INT DEFAULT 0,
    experience_max INT,
    seniority VARCHAR(50), -- ENTRY, MID, SENIOR, LEAD
    industry VARCHAR(100),
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT', -- DRAFT, PUBLISHED, PAUSED, CLOSED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE SET NULL,
    FOREIGN KEY (created_by_user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS resumes (
    id VARCHAR(36) PRIMARY KEY,
    candidate_id VARCHAR(36) NOT NULL,
    title VARCHAR(150) NOT NULL,
    raw_text LONGTEXT,
    file_url VARCHAR(255),
    parsed_content_json LONGTEXT,
    overall_score INT DEFAULT 0,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (candidate_id) REFERENCES candidate_profiles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS applications (
    id VARCHAR(36) PRIMARY KEY,
    job_id VARCHAR(36) NOT NULL,
    candidate_id VARCHAR(36) NOT NULL,
    resume_id VARCHAR(36),
    cover_letter LONGTEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'APPLIED', -- APPLIED, SCREENING, REVIEW, SHORTLISTED, INTERVIEW, OFFER, HIRED, REJECTED
    match_score INT DEFAULT 0,
    match_analysis_json LONGTEXT,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES candidate_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE SET NULL,
    UNIQUE KEY uk_job_candidate (job_id, candidate_id)
);

CREATE TABLE IF NOT EXISTS interviews (
    id VARCHAR(36) PRIMARY KEY,
    application_id VARCHAR(36) NOT NULL,
    candidate_id VARCHAR(36) NOT NULL,
    job_id VARCHAR(36) NOT NULL,
    interview_type VARCHAR(50) NOT NULL, -- TECHNICAL, BEHAVIORAL, MOCK, HR
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, COMPLETED, CANCELLED
    scheduled_at TIMESTAMP NULL,
    duration_minutes INT DEFAULT 45,
    feedback_summary TEXT,
    overall_score INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES candidate_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ai_analyses (
    id VARCHAR(36) PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL, -- RESUME, JOB, MATCH, MOCK_INTERVIEW
    entity_id VARCHAR(36) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    prompt_hash VARCHAR(64),
    response_json LONGTEXT NOT NULL,
    confidence_score INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    organization_id VARCHAR(36),
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50),
    entity_id VARCHAR(36),
    metadata_json LONGTEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
