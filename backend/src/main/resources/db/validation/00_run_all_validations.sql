\echo '== Pasteleria ERP validation: structure =='
\i 01_smoke_structure.sql

\echo '== Pasteleria ERP validation: seeds =='
\i 02_smoke_seeds.sql

\echo '== Pasteleria ERP validation: V1 invariants =='
\i 03_validate_v1_invariants.sql

\echo '== Pasteleria ERP validation: inventory movements =='
\i 04_validate_inventory_movements.sql

\echo '== Pasteleria ERP validation: cash register =='
\i 05_validate_cash_register.sql

\echo '== Pasteleria ERP validation: pedidos/produccion =='
\i 06_validate_order_production_state.sql
\i 07_validate_production_materials.sql
\i 08_validate_purchase_documents.sql

\echo '== ERP validation: third parties =='
\i 09_validate_third_parties.sql

\echo '== ERP validation: receivables, collections and payables =='
\i 10_validate_receivables_payables.sql

\echo '== ERP validation: accounting =='
\i 11_validate_accounting.sql

\echo '== ERP validation: bridges =='
\i 12_validate_erp_bridges.sql

\echo '== ERP validation: fiscal documents =='
\i 13_validate_fiscal_documents.sql

\echo '== ERP validation: intelligence/reporting =='
\i 14_validate_intelligence_reporting.sql

\echo '== ERP validation: presentation/SIT =='
\i 15_validate_presentation_sit.sql

\echo '== ERP validation: audit/support/evidence =='
\i 16_validate_audit_support_evidence.sql

\echo '== Pasteleria ERP validation completed =='
