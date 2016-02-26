-- clean up the accounts_users table
delete from sctid.accounts_users where
(account_id, user_id) in 
(select c.account_id, c.user_id 
from sctid.accounts a, sctid.users b, sctid.accounts_users c
where 
a.id=c.account_id and b.id=c.user_id and 
b.employee_cnum in ('-054LF897','-054LL897','-054LM897','-054LN897','-054LE897','sctid', 'ar01', 'au01', 'au02', 'au03', 'cn01', 'cn02', 'ie01', 'ie02', 'il01', 'in01', 'uk01', 'uk02', 'uk03', 'us01', 'us02', 'us03', 'us04', 'us05', 'jm01', 'jm02'));
