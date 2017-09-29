"# kantindsl" 
Script : domainkantin2
1. Define pelanggan yang akan membeli
2. Lakukan order
3. Setelah selesai transaksi akan tercetak

def pelangganRio = new Pelanggan("Rio")  
order(pelangganRio,"NasiGoreng", 2)  
order(pelangganRio,"AyamGoreng", 2)   
selesaiOrder(pelangganRio)  
cetakTransaksi()  

