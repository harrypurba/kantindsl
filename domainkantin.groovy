class Domainkantin{
	static void main(String[] args){
		println("Domain Kantin")
		
		def listPelayan = [new Pelayan("Verisky"), new Pelayan("Rio")]
		def listPelanggan = [new Pelanggan("Rio")]
		def listMenu = [new Menu("Soto")]
		listPelanggan[0].order(listMenu[0], 1, listPelayan[0]);
	}
}

class Pelanggan{
	String nama
	Pelanggan(_nama) {
		nama = _nama
	}
	def order(menu, jumlah, pelayan) {
		println("Pesan " + jumlah + " " + menu.nama + " kepada "  + pelayan.nama)
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
}

class Kasir{
	String nama
	Kasir(_nama) {
		nama = _nama
	}
}

class Menu{
	String nama
	int stok
	Menu(_nama) {
		nama = _nama
	}
}

