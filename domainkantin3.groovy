import java.text.SimpleDateFormat

class Domainkantin {
	def static KASIR

	static void main(String[] args){
		println("Domain Kantin")
		
		// Inisialisasi 
		KASIR = new Kasir("Verisky")
		def kasir = KASIR

		
		// Melakukan pemesanan
		order("Aqua", 101)
		order("NasiGoreng", 2)
		order("AyamGoreng", 2)
		order("Ikan", 1)
		selesaiOrder()
		cetakTransaksi()

		// Melakukan pemesanan
		order("Aqua", 101)
		order("NasiGoreng", 2)
		order("AyamGoreng", 2)
		order("Ikan", 1)
		takeAway()
		selesaiOrder()
		cetakTransaksi()
		cetakTransaksiTerakhir()

		cetakTransaksiBulanKe(9, 2017)
	} 
	
	static def cetakTransaksiBulanKe(i, tahun){
		KASIR.cetakTransaksiBulanKe(i, tahun)
	}

	// Melakukan order
	static def order(menu, jumlah) {
		validasiStateOrder()
		int total = KASIR.order(menu, jumlah)
		if (total != 0) {
			KASIR.getTransaksiTerakhir().add(new Order(DaftarMenu.getMenu(menu), jumlah))
		}
	}

	// validasi jika order baru (dari pelanggan berbeda sebelumnya), maka tambah transaksi baru
	static def validasiStateOrder(){

		// jika belum ada transaksi sama sekali, maka tambah transaksi baru
		if(KASIR.getTransaksiTerakhir() == null)
			KASIR.tambahTransaksiBaru()

		// jika transaksi sebelumnya sudah selesai, maka tambah transaksi baru
		if(KASIR.getTransaksiTerakhir().isTransaksiSelesai())
			KASIR.tambahTransaksiBaru()
	}
	
	// menyelesaikan order
	static def selesaiOrder() {
		if(!stateSelesaiOrderOK())
			return

		def total = KASIR.getTransaksiTerakhir().getTotal()
		println("Total harga " + total)
		KASIR.getTransaksiTerakhir().transaksiSelesai()
	}

	// validasi apakah pelanggan dapat menyelesaikan order atau tidak
	static def stateSelesaiOrderOK(){

		// jika belum ada transaksi sama sekali, semantik salah
		if(KASIR.getTransaksiTerakhir() == null){
			println("Anda belum pernah melakukan order, silahkan order terlebih dahulu")
			return false
		}

		// jika transaksi sebelumnya sudah selesai, maka tambah transaksi baru
		if(KASIR.getTransaksiTerakhir().isTransaksiSelesai()){
			println("Anda belum melakukan order, silahkan order terlebih dahulu")
			return false
		}

		return true
	}

	static def cetakTransaksi(){
		KASIR.cetakTransaksi()
	}

	static def cetakTransaksiTerakhir(){
		KASIR.cetakTransaksiTerakhir()
	}

	static def takeAway(){
		KASIR.takeAway()
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
	Boolean sudahMencetakTransaksiTerakhir = true
	Kasir(_nama) {
		nama = _nama
	}

	def tambahTransaksiBaru() {
		def index = listTransaksi.size()
		listTransaksi.add(new Transaksi(index))
		sudahMencetakTransaksiTerakhir = false
	}
	
	def daftarMenu = new DaftarMenu();
	def order(menu, jumlah) {
		def foundMenu = daftarMenu.getMenu(menu);
		def hargaSatuan = 0
		if (foundMenu) {
			hargaSatuan = foundMenu.harga
			if (foundMenu.stok > jumlah) {
				println("Pesan " + jumlah + " " + menu + " " + hargaSatuan + " kepada "  + nama)
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
		if(getTransaksiTerakhir() == 0){
			println("Anda belum melakukan transaksi apapun")
			return
		}

		if(sudahMencetakTransaksiTerakhir){
			println("Transaksi sebelumnya telah dicetak, gunakan cetakTransaksiTerakhir() untuk mencetak ulang")
			return
		}

		if(!getTransaksiTerakhir().isTransaksiSelesai()){
			println("Order anda belum selesai, silahkan menyelesaikan order terlebih dahulu")
			return
		}

		getTransaksiTerakhir().print()
		sudahMencetakTransaksiTerakhir = true
	}

	def takeAway(){
		getTransaksiTerakhir().takeAway()
	}

	def cetakTransaksiTerakhir() {
		println ("+-------- CETAK ULANG BEGIN ----------")
		println ("|")
		getTransaksiTerakhir().printAgain()
		sudahMencetakTransaksiTerakhir = true
		println ("+-------- CETAK ULANG END   ----------")
	}

	def getTransaksiTerakhir() {
		if(listTransaksi.size() == 0)
			return null
		else
			return listTransaksi[-1]
	}

	def cetakTransaksiBulanKe(i, tahun){
		List<Transaksi> listTransaksiBulanan = []
		listTransaksi.eachWithIndex { it, index ->
			if(it.getYearTransaction() == tahun && it.getMonthTransaction() == i)
				listTransaksiBulanan.add(it)
		}

		println ""
		println ""
		println "+"
		println ("| Laporan Transaksi Bulan ke-" + i + " Tahun " + tahun)
		println "+"
		if(listTransaksiBulanan.size() == 0)
			println ("+ TIDAK ADA TRANSAKSI ----------")
		else
			listTransaksiBulanan.eachWithIndex{ it, index ->
				it.printReport();
			}
		println ("+ Laporan Transaksi Bulanan END   ----------")
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
	Boolean isTakeAway = false
	Date transactionDate

	Transaksi(i){
		indexOrder = i
	}

	def takeAway(){
		isTakeAway = true
	}

	def getTakeAwayStatus(){
		return isTakeAway
	}

	def setTransactionDate(Date date){
		transactionDate = date
	}

	def getTransactionDate(){
		return transactionDate
	}

	def getMonthTransaction(){
		return transactionDate.getMonth() + 1
	}

	def getYearTransaction(){
		return (((int) (transactionDate.getYear() / 100) + 19) * 100) + (transactionDate.getYear() % 100);
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
		setTransactionDate(date)
		println getYearTransaction()
		def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
		println sdf.format(date) + (isTakeAway ? " Take away" : " Dine-in")
		listOrder.each {
			item -> println(item.jumlah + " " + item.menu.nama + " @" +  item.menu.harga 
			+ "\t" + item.menu.harga*item.jumlah)
		}
		println("\t\t\t" + getTotal())
		println ""
		println ""
	}

	def printAgain() {
		println ("| ---------Transaction #" + indexOrder + "----------")
		def date = new Date()
		def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
		println "| " + sdf.format(date) + (isTakeAway ? " Take away" : " Dine-in")
		listOrder.each {
			item -> println("| " + item.jumlah + " " + item.menu.nama + " @" +  item.menu.harga 
			+ "\t" + item.menu.harga*item.jumlah)
		}
		println("| " + "\t\t\t" + getTotal())
		println "| "
		println "| "
	}

	def printReport() {
		println ("##---------Transaction #" + indexOrder + "----------")
		def date = new Date()
		def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
		println "# " + sdf.format(date) + (isTakeAway ? " Take away" : " Dine-in")
		listOrder.each {
			item -> println("# " + item.jumlah + " " + item.menu.nama + " @" +  item.menu.harga 
			+ "\t" + item.menu.harga*item.jumlah)
		}
		println("# " + "\t\t\t" + getTotal())
		println "# "
		println "##"
	}
}
