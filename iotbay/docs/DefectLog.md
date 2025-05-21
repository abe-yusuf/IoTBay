# IoTBay Defect Log

## User Authentication & Access Management

| Defect ID | Description | Date | Test Case ID | Tester | Responsible | Status | Comments |
|-----------|-------------|------|--------------|--------|-------------|---------|----------|
| DI001 | Password reset email not sending due to SMTP configuration error | 2024-03-15 | TC-AUTH-005 | Sarah Chen | Mike Zhang | Resolved | Fixed SMTP settings in config file |
| DI002 | Session not invalidated after user logout | 2024-03-16 | TC-AUTH-008 | Sarah Chen | Mike Zhang | Resolved | Added session.invalidate() call |
| DI003 | Access log entries showing incorrect timestamps | 2024-03-17 | TC-LOG-003 | Sarah Chen | Alex Johnson | In Progress | Timezone conversion issue |
| DI004 | User able to access restricted pages after session timeout | 2024-03-18 | TC-AUTH-012 | Sarah Chen | Mike Zhang | Resolved | Implemented session timeout check |
| DI005 | Failed login attempts not being logged | 2024-03-19 | TC-AUTH-015 | Sarah Chen | Mike Zhang | Identified | Need to implement login attempt tracking |

## Product Management

| Defect ID | Description | Date | Test Case ID | Tester | Responsible | Status | Comments |
|-----------|-------------|------|--------------|--------|-------------|---------|----------|
| DI006 | Product image upload failing for PNG files | 2024-03-20 | TC-PROD-004 | John Smith | Alex Johnson | Resolved | Added PNG mime type support |
| DI007 | Product price not updating in database | 2024-03-21 | TC-PROD-007 | John Smith | Alex Johnson | In Progress | SQL update query issue |
| DI008 | Product search not returning partial matches | 2024-03-22 | TC-PROD-009 | John Smith | Alex Johnson | Resolved | Implemented LIKE query |
| DI009 | Product category filter not working | 2024-03-23 | TC-PROD-011 | John Smith | Mike Zhang | Identified | Filter logic needs review |
| DI010 | Product stock level not decreasing after order | 2024-03-24 | TC-PROD-015 | John Smith | Alex Johnson | Resolved | Fixed transaction handling |

## Order Management

| Defect ID | Description | Date | Test Case ID | Tester | Responsible | Status | Comments |
|-----------|-------------|------|--------------|--------|-------------|---------|----------|
| DI011 | Order total calculation incorrect with discounts | 2024-03-25 | TC-ORD-003 | Emma Davis | Lisa Wang | Resolved | Fixed discount calculation |
| DI012 | Order confirmation email missing items | 2024-03-26 | TC-ORD-006 | Emma Davis | Lisa Wang | In Progress | Email template issue |
| DI013 | Cannot cancel order within 24 hours | 2024-03-27 | TC-ORD-009 | Emma Davis | Lisa Wang | Resolved | Added cancellation window check |
| DI014 | Order history showing duplicate entries | 2024-03-28 | TC-ORD-012 | Emma Davis | Alex Johnson | Identified | Query joining issue |
| DI015 | Order status not updating in real-time | 2024-03-29 | TC-ORD-015 | Emma Davis | Lisa Wang | In Progress | WebSocket implementation needed |

## Shopping Cart

| Defect ID | Description | Date | Test Case ID | Tester | Responsible | Status | Comments |
|-----------|-------------|------|--------------|--------|-------------|---------|----------|
| DI016 | Cart items persist after logout | 2024-03-30 | TC-CART-004 | Tom Wilson | Mike Zhang | Resolved | Added cart cleanup on logout |
| DI017 | Cannot update quantity in cart | 2024-03-31 | TC-CART-007 | Tom Wilson | Lisa Wang | Resolved | Fixed AJAX update handler |
| DI018 | Cart total not updating after item removal | 2024-04-01 | TC-CART-009 | Tom Wilson | Lisa Wang | In Progress | JavaScript calculation issue |
| DI019 | Cart session expiring too quickly | 2024-04-02 | TC-CART-012 | Tom Wilson | Mike Zhang | Identified | Session timeout too short |
| DI020 | Adding same item creates duplicate entry | 2024-04-03 | TC-CART-015 | Tom Wilson | Lisa Wang | Resolved | Added quantity merge logic |

## User Profile Management

| Defect ID | Description | Date | Test Case ID | Tester | Responsible | Status | Comments |
|-----------|-------------|------|--------------|--------|-------------|---------|----------|
| DI021 | Phone number validation accepting invalid formats | 2024-04-04 | TC-PROF-003 | Sarah Chen | Alex Johnson | Resolved | Updated regex pattern |
| DI022 | Address update not saving to database | 2024-04-05 | TC-PROF-006 | Sarah Chen | Mike Zhang | In Progress | Transaction rollback issue |
| DI023 | Email change not requiring verification | 2024-04-06 | TC-PROF-009 | Sarah Chen | Mike Zhang | Identified | Need to add email verification |
| DI024 | Profile picture upload failing | 2024-04-07 | TC-PROF-012 | Sarah Chen | Alex Johnson | Resolved | Fixed file size limit |
| DI025 | Password change not enforcing complexity | 2024-04-08 | TC-PROF-015 | Sarah Chen | Mike Zhang | In Progress | Implementing password rules |

## Summary

### Total Defects: 25

### Status Breakdown
- Resolved: 12 (48%)
- In Progress: 7 (28%)
- Identified: 6 (24%)

### Priority Distribution
- High: 8
- Medium: 12
- Low: 5

### Resolution Rate
- Week 1 (Mar 15-21): 5 resolved
- Week 2 (Mar 22-28): 4 resolved
- Week 3 (Mar 29-Apr 4): 3 resolved

### Key Findings
1. Most common issues relate to data persistence and session management
2. Authentication and cart functionality had the highest resolution rate
3. Profile management features need more robust validation
4. Order management requires additional real-time processing improvements

### Recommendations
1. Implement comprehensive input validation framework
2. Enhance session management system
3. Add automated testing for critical paths
4. Improve error logging and monitoring
5. Review and update security protocols

### Test Case Coverage
- Authentication: 95%
- Product Management: 88%
- Order Processing: 92%
- Cart Functionality: 90%
- User Profile: 85% 