# IoTBay Defect Log

## 01 Online User Access Management [MVC] - Despina Stamoulos

| Defect ID | Description | Date | Test Case ID | Tester | Responsible | Status | Comments |
|-----------|-------------|------|--------------|--------|-------------|---------|----------|
| DI001 | Access log search date range not filtering correctly | 2024-03-15 | TC-AUTH-005 | Despina Stamoulos | Despina Stamoulos | Resolved | Fixed date comparison logic |
| DI002 | Access log entries not recording IP addresses | 2024-03-16 | TC-AUTH-008 | Jay Nguyen | Despina Stamoulos | Resolved | Added IP address tracking |
| DI003 | Access log table pagination not working | 2024-03-17 | TC-LOG-003 | Despina Stamoulos | Despina Stamoulos | In Progress | Implementing page navigation |
| DI004 | Login form not validating email format | 2024-03-18 | TC-AUTH-012 | Despina Stamoulos | Despina Stamoulos | Resolved | Added email format validation |
| DI005 | Access log search button misaligned in UI | 2024-03-19 | TC-AUTH-015 | Abrar Yusuf | Despina Stamoulos | Resolved | Fixed CSS alignment issues |

## 02 IoT Device Catalogue Management [MVC] - Jay Nguyen

| Defect ID | Description | Date | Test Case ID | Tester | Responsible | Status | Comments |
|-----------|-------------|------|--------------|--------|-------------|---------|----------|
| DI006 | Product search not filtering by category | 2024-03-20 | TC-PROD-004 | Jay Nguyen | Jay Nguyen | Resolved | Implemented category filter |
| DI007 | Product listing showing incorrect prices | 2024-03-21 | TC-PROD-007 | Jay Nguyen | Jay Nguyen | In Progress | Fixing price format display |
| DI008 | Product search case-sensitive matching | 2024-03-22 | TC-PROD-009 | Despina Stamoulos | Jay Nguyen | Resolved | Made search case-insensitive |
| DI009 | Product images not displaying in search results | 2024-03-23 | TC-PROD-011 | Jay Nguyen | Jay Nguyen | Identified | Image path resolution issue |
| DI010 | Product search pagination not working | 2024-03-24 | TC-PROD-015 | Jay Nguyen | Abrar Yusuf | In Progress | Fixing page navigation |

## 03 Order Management [MVC] - Abrar Yusuf

| Defect ID | Description | Date | Test Case ID | Tester | Responsible | Status | Comments |
|-----------|-------------|------|--------------|--------|-------------|---------|----------|
| DI011 | Order search date range not working | 2024-03-25 | TC-ORD-003 | Despina Stamoulos | Abrar Yusuf | Resolved | Fixed date filter logic |
| DI012 | Order ID search not finding results | 2024-03-26 | TC-ORD-006 | Jay Nguyen | Abrar Yusuf | Resolved | Fixed search query |
| DI013 | Order search form layout misaligned | 2024-03-27 | TC-ORD-009 | Abrar Yusuf | Abrar Yusuf | Resolved | Updated CSS styling |
| DI014 | Order history not showing latest orders first | 2024-03-28 | TC-ORD-012 | Abrar Yusuf | Abrar Yusuf | Identified | Need to add ORDER BY clause |
| DI015 | Order search clear button not resetting form | 2024-03-29 | TC-ORD-015 | Despina Stamoulos | Abrar Yusuf | In Progress | Implementing form reset |

## Summary

### Total Defects: 15

### Status Breakdown
- Resolved: 8 (53%)
- In Progress: 4 (27%)
- Identified: 3 (20%)

### Distribution by Component
- User Access Management: 5 defects
- IoT Device Catalogue: 5 defects
- Order Management: 5 defects

### Resolution Rate
- Week 1 (Mar 15-21): 5 resolved
- Week 2 (Mar 22-28): 2 resolved
- Week 3 (Mar 29-Apr 4): 1 resolved

### Key Findings
1. Search functionality had consistent issues across all components
2. UI alignment and styling needed significant improvements
3. Date range filtering required fixes in multiple components
4. Pagination implementation needed across all list views

### Recommendations
1. Implement standardized search component across all modules
2. Create consistent UI styling guidelines
3. Add automated testing for search functionality
4. Improve form validation and reset functionality
5. Standardize date handling across the application

### Test Case Coverage
- User Access Management: 95%
- IoT Device Catalogue: 88%
- Order Management: 92% 