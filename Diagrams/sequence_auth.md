[//]: # (Install Mermaid plugin to view solution)
```mermaid
sequenceDiagram
    participant Client
    participant AuthController
    participant CustomUserDetailsService
    participant JwtUtil
    participant UserRepository
    participant JwtRequestFilter
    
    %% Login Flow
    Client->>AuthController: POST /auth/login (AuthenticationRequest)
    AuthController->>CustomUserDetailsService: loadUserByUsername(username)
    CustomUserDetailsService->>UserRepository: findByUsername(username)
    UserRepository-->>CustomUserDetailsService: User
    CustomUserDetailsService-->>AuthController: UserDetails
    
    AuthController->>AuthController: Validate password
    AuthController->>JwtUtil: generateToken(username)
    JwtUtil-->>AuthController: JWT Token
    AuthController-->>Client: AuthenticationResponse(token, username, role)
    
    %% Protected Endpoint Flow
    Client->>JwtRequestFilter: Request with JWT Token
    JwtRequestFilter->>JwtUtil: validateToken(token)
    JwtUtil-->>JwtRequestFilter: isValid
    
    alt Valid Token
        JwtRequestFilter->>CustomUserDetailsService: loadUserByUsername(username)
        CustomUserDetailsService->>UserRepository: findByUsername(username)
        UserRepository-->>CustomUserDetailsService: User
        CustomUserDetailsService-->>JwtRequestFilter: UserDetails
        JwtRequestFilter->>JwtRequestFilter: Set SecurityContext
        JwtRequestFilter-->>Client: Access Granted
    else Invalid Token
        JwtRequestFilter-->>Client: 401 Unauthorized
    end
```

This sequence diagram illustrates two main flows:
1. **Authentication Flow (Login)**:
    - Client sends login credentials to `/auth/login`
    - AuthController receives the request and validates credentials
    - CustomUserDetailsService loads user details from the repository
    - JwtUtil generates a JWT token upon successful authentication
    - Client receives the token and user information

2. **Protected Endpoint Flow**:
    - Client sends a request with JWT token in header
    - JwtRequestFilter intercepts the request
    - JwtUtil validates the token
    - If valid, loads user details and sets security context
    - If invalid, returns 401 Unauthorized
