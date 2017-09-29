class Domainkantin{
	static def listPelayan
	static def sum = 0;
	
	static void main(String[] args){
		println("Domain Kantin")
		
		listPelayan = [new Pelayan("Verisky"), new Pelayan("Rio")]
		order("Aqua", 1)
		order("NasiGoreng", 2)
		order("Ikan", 1)
		selesaiOrder()
	}
	static def order(menu, jumlah) {
		sum += listPelayan[0].order(menu, jumlah)
	}
	static def selesaiOrder() {
		println("Total harga " + sum)
		sum = 0;
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
	def daftarMenu = new DaftarMenu();
	Pelayan(_nama) {
		nama = _nama
	}
	def order(menu, jumlah) {
		def hargaSatuan = daftarMenu.getHarga(menu);
		if (hargaSatuan) {
			println("Pesan " + jumlah + " " + menu + " " + hargaSatuan + " kepada "  + nama)
		}
		else {
			println("Menu " + menu + " tidak tersedia")
		}
		return hargaSatuan*jumlah
	}
}

class Kasir{
	String nama
	Kasir(_nama) {
		nama = _nama
	}
}

class Menu{
	String nama
	int harga
	Menu(_nama, _harga) {
		nama = _nama
		harga = _harga
	}
}

class Kantin{
	String nama
	Kantin(_nama) {
		nama = _nama
	}
}

class DaftarMenu{
	static List<Menu> listMenu
	DaftarMenu() {
		listMenu = [
			new Menu("Nasi", 4000),
			new Menu("NasiGoreng", 8000),
			new Menu("Soto", 12000),
			new Menu("AyamGoreng", 10000),
			new Menu("TelurDadar", 5000),
			new Menu("Aqua", 3000),
			new Menu("TehPucuk", 4000),
			new Menu("JusJambu", 8000),
		]
	}
	def getHarga(String nama) {
		def menu = listMenu.find{item -> item.nama == nama};
		def harga = 0;
		if (menu)
			harga = menu.harga
		return harga
	}
}

