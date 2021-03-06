import java.text.SimpleDateFormat

class Domainkantin{
	static def kasir
	static def pelayan
	static def transaksi
	
	static void main(String[] args){
		println("Domain Kantin")
		
		// Inisialisasi 
		kasir = new Kasir("Verisky")
		pelayan = new Pelayan("Rio")
		transaksi = new Transaksi()
		
		// Melakukan pemesanan
		order("Aqua", 101)
		order("NasiGoreng", 2)
		order("AyamGoreng", 2)
		order("Ikan", 1)
		selesaiOrder()
		transaksi.print()
		pelayan.siapkanPesanan(transaksi)
	} 
	
	static def order(menu, jumlah) {
		int total = kasir.order(menu, jumlah)
		if (total != 0) {
			transaksi.add(new Order(DaftarMenu.get(menu), jumlah))
		}
	}
	
	static def selesaiOrder() {
		println("Total harga " + transaksi.getTotal())
		transaksi = 
	}
}

class Pelanggan{
	String nama
	Pelanggan(_nama) {
		nama = _nama
	}
}

class Jurumasak{
	String nama
	Jurumasak(_nama) {
		nama = _nama
	}
}

class Pelayan{
	String nama
	Pelayan(_nama) {
		nama = _nama
	}
	def siapkanPesanan(transaksi) {
		// Dalam sebuah transaksi siapkan semua pesanannya
		transaksi.listOrder.each {
			item -> item.menu.listBahanBaku.each {
				bahanBaku -> bahanBaku.stok = bahanBaku.stok - 1 
			}
		}
		println("Pesanan sudah siap.")
	}
}

class Kasir{
	String nama
	Kasir(_nama) {
		nama = _nama
	}
		def daftarMenu = new DaftarMenu();
	def order(menu, jumlah) {
		def foundMenu = daftarMenu.get(menu);
		def hargaSatuan = 0
		if (foundMenu) {
			hargaSatuan = foundMenu.harga
			if (foundMenu.stok > jumlah) {
				println("Pesan " + jumlah + " " + menu + " " + hargaSatuan + " kepada "  + nama)
				foundMenu.stok -= jumlah;
			}
			else { // Pesanan melebihi stok tersisa
				println("Hanya tersedia stok " + foundMenu.nama + " : " + foundMenu.stok) 
				return 0
			}
				
		}
		else { // Pesanan tidak tersedia di menu
			println("Menu " + menu + " tidak tersedia")
		}
		return hargaSatuan*jumlah
	}
}

class Menu{
	String nama
	int harga
	int stok
	static DaftarBahanBaku daftarBahanBakuTersedia = new DaftarBahanBaku()
	List<BahanBaku> listBahanBaku = []
	
	Menu(_nama, _harga, _stok, List<String> _listBahanBaku) {
		nama = _nama
		harga = _harga
		stok = _stok
		_listBahanBaku.each {
			item->listBahanBaku.add(daftarBahanBakuTersedia.get(item))
		}
	}
}

class Kantin{
	String nama
	Kantin(_nama) {
		nama = _nama
	}
}

class BahanBaku{
	String nama
	int stok
	BahanBaku(_nama, _stok) {
		nama = _nama
		stok = _stok
	}
}

class DaftarBahanBaku{
	static List<Menu> listBahanBaku = [
			new BahanBaku("Beras", 50),
			new BahanBaku("MinyakGoreng", 10),
			new BahanBaku("DagingAyam", 50),
			new BahanBaku("Telur", 100),
			new BahanBaku("JambuBiji", 20),
			new BahanBaku("Kecap", 10),
			new BahanBaku("Garam", 20),
		]
	DaftarBahanBaku() {
		
	}
	static def get(String nama) {
		def bahanBaku = listBahanBaku.find{item -> item.nama == nama}
		if (bahanBaku==null) {
			println("Bahan baku " + nama + " tidak tersedia.");
		}
		return bahanBaku
	}
}

class DaftarMenu{
	static List<Menu> listMenu = [
			new Menu("Nasi", 4000, 50, ["Beras"]),
			new Menu("NasiGoreng", 8000, 55, ["Beras", "Garam", "Kecap"]),
			new Menu("Soto", 12000, 20, ["DagingAyam", "Garam"]),
			new Menu("AyamGoreng", 10000, 50, ["DagingAyam", "MinyakGoreng", "Garam"]),
			new Menu("TelurDadar", 5000, 75, ["Telur", "MinyakGoreng", "Garam"]),
			new Menu("Aqua", 3000, 100, []),
			new Menu("TehPucuk", 4000, 30, []),
			new Menu("JusJambu", 8000, 5, ["JambuBiji"]),
		]
	DaftarMenu() {
		
	}
	static def get(String nama) {
		def menu = listMenu.find{item -> item.nama == nama}
		return menu
	}
}

class Order {
	Menu menu
	int jumlah
	Order(_menu, _jumlah) {
		menu = _menu
		jumlah = _jumlah
	}
}

class Transaksi{
	List<Order> listOrder = []
	def add(menu) {
		listOrder.add(menu)
	}
	def getTotal() {
		int total = 0;
		listOrder.each {
			item -> total+=item.menu.harga*item.jumlah
		}
		return total
	}
	def print() {
		println()
		println ("---------Transaction----------")
		def date = new Date()
		def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
		println sdf.format(date)
		listOrder.each {
			item -> println(item.jumlah + " " + item.menu.nama + " @" +  item.menu.harga 
			+ "\t" + item.menu.harga*item.jumlah)
		}
		println("\t\t\t" + getTotal())
	}
}
