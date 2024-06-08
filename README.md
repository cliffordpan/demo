# Demo Project

### Move to home lab - k8s
Using AWS Route53 to host the zone, use a CronJob as ddns.

### Database - H2 database
For demo purpose, this one use H2 memory database with preset
data.

### Controller - Only implemented limit features
- AuthController for access and refresh tokens generation
- ProductController for product CRUD operations
- TicketController for tickets creation, retrieving ,assignment and closing

### Security
- Roles - ADMIN, SUPPORT, CLIENT
- For products reading is opened for all(include anonymous)
- Tickets will be filtered by security principal
    - ADMIN can access and operate all tickets
    - SUPPORT can only access and operate assigned tickets to specified account
    - CLIENT can only access created tickets
- Self api call is for getting logged profile