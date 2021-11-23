<h1>Only-Pawns-Chess</h1>
<i>Terminal game for 2 players</i><br>
Regular <a href="https://en.wikipedia.org/wiki/Chess" >chess</a> but all sides have only pawns<br>
<h3>Rules</h3>
<p>This game extend all the rules of regular chess but it has one more rule:</p>
<ul>
  <li>If pawn of some side is on last line, then that side is winner!</li>
</ul>

<h3>Snippets of code</h3>

This class is used for count pawns on the board

<pre lang="kotlin">
 enum class Pawns(val color: String, var left: Int) {
    WHITE("White", 8),
    BLACK("Black", 8);

    fun beat() {
        left--
    }
 }
 
</pre>

This snippet of code used to create board

<pre lang="kotlin">
val boldLine = "  +---+---+---+---+---+---+---+---+"
val filesLine = "    a   b   c   d   e   f   g   h"
val files = "abcdefgh"
val regex = """[a-h][1-8][a-h][1-8]\b""".toRegex()
val blackPlayer = "| B "
val whitePlayer = "| W "
var chosenPlayer = whitePlayer
val emptySlot = "|   "
val blackPlayerStartingIndex = 7
val whitePlayerStartingIndex = 2
var invalidInput = false
var whitePassant = false
var blackPassant = false
var currentCoords = ""
//chessboard initialization
val chessboard = mutableListOf(
    MutableList(8) { "|   " }, //8
    MutableList(8) { blackPlayer },//7
    MutableList(8) { "|   " }, //6
    MutableList(8) { "|   " }, //5
    MutableList(8) { "|   " }, //4
    MutableList(8) { "|   " }, //3
    MutableList(8) { whitePlayer }, //2
    MutableList(8) { "|   " } //1
).reversed()
</pre>

This is start of the game

<pre lang="kotlin">
println(" Pawns-Only Chess")
println("First Player's name: ")
val player1 = readLine()!!
println("Second Player's name: ")
val player2 = readLine()!!
var currentTurn = player1
</pre>

This is printing result of the game

<pre lang="kotlin">
fun ending(res: String) {
    printBoard()
    when (res) {
        "P1" -> {
            currentTurn = "exit"
            println("White wins!")
            println("Bye!")
        }
        "P2" -> {
            currentTurn = "exit"
            println("Black wins!")
            println("Bye!")
        }
        else -> {
            currentTurn = "exit"
            println("Stalemate!")
            println("Bye!")
        }
    }
}
</pre>
