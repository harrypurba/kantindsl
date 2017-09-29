import java.text.SimpleDateFormat

class Domainkantin {
	def static daftarKasir = []

	static void main(String[] args){
		println("Domain Kantin")
		
		// Inisialisasi 
		def kasirVerisky = new Kasir("Verisky")
		daftarKasir.add(kasirVerisky)
		
		def pelangganRio = new Pelanggan("Rio")

		// Pelanggan Rio Melakukan pemesanan
		order(pelangganRio,"Aqua", 101)
		order(pelangganRio,"NasiGoreng", 2)
		order(pelangganRio,"AyamGoreng", 2)
		order(pelangganRio,"Ikan", 1)
		selesaiOrder(pelangganRio)
		cetakTransaksiX()

		def pelangganHarry = new Pelanggan("Harry")

		println()
		println("===================================")
		println()
		println()
		
		// Pelanggan Harry Melakukan pemesanan
		order(pelangganHarry,"Aqua", 101)
		order(pelangganHarry,"NasiGoreng", 2)
		order(pelangganHarry,"AyamGoreng", 2)
		order(pelangganHarry,"Ikan", 1)
		selesaiOrder(pelangganHarry)
		cetakTransaksiX()

		selesaiOrder()
	} 
	
	static def order(pelanggan, menu, jumlah) {
		int total = daftarKasir[0].order(pelanggan, menu, jumlah)
		if (total != 0) {

			// jika belum ada transaksi sama sekali, maka tambah transaksi baru
			if(daftarKasir[0].getTransaksiTerakhir() == null)
				daftarKasir[0].tambahTransaksiBaru()

			// jika transaksi sebelumnya sudah selesai, maka tambah transaksi baru
			if(daftarKasir[0].getTransaksiTerakhir().isTransaksiSelesai())
				daftarKasir[0].tambahTransaksiBaru()

			daftarKasir[0].getTransaksiTerakhir().add(new Order(DaftarMenu.getMenu(menu), jumlah))
		}
	}
	
	static def selesaiOrder(pelanggan) {
		// jika belum ada transaksi sama sekali, semantik salah
		if(daftarKasir[0].getTransaksiTerakhir() == null){
			println("Anda belum pernah melakukan order, silahkan order terlebih dahulu")
			return
		}

		// jika transaksi sebelumnya sudah selesai, maka tambah transaksi baru
		if(daftarKasir[0].getTransaksiTerakhir().isTransaksiSelesai()){
			println("Anda belum melakukan order, silahkan order terlebih dahulu")
			return
		}

		def total = daftarKasir[0].getTransaksiTerakhir().getTotal()
		println("Total harga " + total)
		daftarKasir[0].getTransaksiTerakhir().transaksiSelesai()
	}

	static def cetakTransaksiX(){
		def lastTransc = daftarKasir[0].getTransaksiTerakhir()
		if(lastTransc.isTransaksiSelesai())
			lastTransc.print()
		else
			println("Order anda belum diselesaikan")
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
}

class Kasir{
	String nama
	List<Transaksi> listTransaksi = []
	Kasir(_nama) {
		nama = _nama
	}

	def tambahTransaksiBaru() {
		def index = listTransaksi.size()
		listTransaksi.add(new Transaksi(index))
	}
	
	def daftarMenu = new DaftarMenu();
	def order(pelanggan, menu, jumlah) {
		def foundMenu = daftarMenu.getMenu(menu);
		def hargaSatuan = 0
		if (foundMenu) {
			hargaSatuan = foundMenu.harga
			if (foundMenu.stok > jumlah) {
				println("Pesan " + jumlah + " " + menu + " " + hargaSatuan + " kepada "  + pelanggan.nama)
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

	def cetakTransaksi() {

	}

	def getTransaksiTerakhir() {
		if(listTransaksi.size() == 0)
			return null
		else
			return listTransaksi[-1]
	}
}

class Menu{
	String nama
	int harga
	int stok
	
	Menu(_nama, _harga, _stok) {
		nama = _nama
		harga = _harga
		stok = _stok
	}
}

class DaftarMenu{
	static List<Menu> listMenu = [
			new Menu("Nasi", 4000, 50),
			new Menu("NasiGoreng", 8000, 55),
			new Menu("Soto", 12000, 20),
			new Menu("AyamGoreng", 10000, 50),
			new Menu("TelurDadar", 5000, 75),
			new Menu("Aqua", 3000, 100),
			new Menu("TehPucuk", 4000, 30),
			new Menu("JusJambu", 8000, 5),
		]
	DaftarMenu() {
		
	}
	static def getMenu(String nama) {
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
	Integer indexOrder
	Boolean transaksiSelesai = false

	Transaksi(i){
		indexOrder = i
	}

	def setIndexOrder(Integer i){
		indexOrder = i
	}

	def getIndexOrder(){
		return indexOrder
	}

	def add(menu) {
		listOrder.add(menu)
	}

	def isTransaksiSelesai(){
		return transaksiSelesai == true
	}

	def transaksiSelesai() {
		transaksiSelesai = true
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
		println ("---------Transaction #" + indexOrder + "----------")
		def date = new Date()
		def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
		println sdf.format(date)
		listOrder.each {
			item -> println(item.jumlah + " " + item.menu.nama + " @" +  item.menu.harga 
			+ "\t" + item.menu.harga*item.jumlah)
		}
		println("\t\t\t" + getTotal())
		println ""
		println ""
	}
}
