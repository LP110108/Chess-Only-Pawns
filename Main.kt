package chess

enum class Pawns(val color: String, var left: Int) {
    WHITE("White", 8),
    BLACK("Black", 8);

    fun beat() {
        left--
    }
}

fun main() {
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

    val black = Pawns.BLACK
    val white = Pawns.WHITE
    val listOfWhitePawns: MutableList<String> = mutableListOf("l", "l", "l", "l", "l", "l", "l", "l")
    var blackPawnCounter = 0
    val listOfBlackPawns: MutableList<String> = mutableListOf("l", "l", "l", "l", "l", "l", "l", "l")
    var whitePawnCounter = 0

    //Game Start, reading players names.
    println(" Pawns-Only Chess")
    println("First Player's name: ")
    val player1 = readLine()!!
    println("Second Player's name: ")
    val player2 = readLine()!!
    var currentTurn = player1

    //Functions
    fun printBoard() {
        println(boldLine)
        for (index in chessboard.indices.reversed()) {
            print(index + 1)
            print(" ")
            print(chessboard[index].joinToString().replace(", ", ""))
            print("|")
            print("\n")
            print(boldLine)
            print("\n")
        }
        println(filesLine)
    }

    fun playerChooser() {
        chosenPlayer = if (chosenPlayer == blackPlayer) {
            whitePlayer
        } else {
            blackPlayer
        }
    }

    fun playerTurn() {
        currentTurn = if (currentTurn == player1) {
            player2
        } else {
            player1
        }
    }


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

    fun isItWin(c4: Int) {
        if (c4 == 8 && currentTurn == player1) { // White pawn is on 8th line
            ending("P1")
        } else if (c4 == 1 && currentTurn == player2) { // Black pawn is on 1st line
            ending("P2")
        } else if (black.left == 0) { // Black lost all pawns
            ending("P1")
        } else if (white.left == 0) { // White lost all pawns
            ending("P2")
        } else if (listOfWhitePawns[7] == "b" || listOfWhitePawns[7] == "s") {
            for (results in listOfWhitePawns) {
                if (results == "s") {
                    ending("S")
                }
            }
        } else if (listOfBlackPawns[7] == "b" || listOfBlackPawns[7] == "s") {
            for (results in listOfBlackPawns) {
                if (results == "s") {
                    ending("S")
                }
            }
        } else { // Continue
            playerChooser()
            playerTurn()
            invalidInput = false
        }
    }

    fun moveExecutioner(c1: Int, c2: Int, c3: Int, c4: Int) {
        if (blackPassant && currentTurn == player1 && c2 == 5 && chessboard[c4 - 2][c3] == blackPlayer) {
            chessboard[c2 - 1].removeAt(c1)
            chessboard[c2 - 1].add(c1, emptySlot)//remove
            chessboard[c4 - 2].removeAt(c3) //then add
            chessboard[c4 - 2].add(c3, emptySlot)
            chessboard[c4 - 1].removeAt(c3)
            chessboard[c4 - 1].add(c3, chosenPlayer)
            blackPassant = false
        } else if (whitePassant && currentTurn == player2 && c2 == 4 && chessboard[c4][c3] == whitePlayer) {
            chessboard[c2 - 1].removeAt(c1)
            chessboard[c2 - 1].add(c1, emptySlot)//remove
            chessboard[c4].removeAt(c3) //then add
            chessboard[c4].add(c3, emptySlot)
            chessboard[c4 - 1].removeAt(c3)
            chessboard[c4 - 1].add(c3, chosenPlayer)
            blackPassant = false
        } else {
            chessboard[c2 - 1].removeAt(c1)
            chessboard[c2 - 1].add(c1, emptySlot)//remove
            chessboard[c4 - 1].removeAt(c3) //then add
            chessboard[c4 - 1].add(c3, chosenPlayer)
            if (blackPassant && currentTurn == player1) { // white passant reset
                blackPassant = false
            }
            if (whitePassant && currentTurn == player2) { // black passant reset
                whitePassant = false
            }
        }

        isItWin(c4)
    }


    //Handling Moves
    fun moveHandler(c1: Int, c2: Int, c3: Int, c4: Int) {
        if (currentTurn == player1) { // +-+-+PLAYER 1+-+-+
            if (chessboard[c2 - 1][c1] != whitePlayer) { // is player here?
                println("No white pawn at $currentCoords")
                invalidInput = true
            } else if (c1 == c3 && c2 >= c4) { // cannot move backward
                invalidInput = true
                println("Invalid Input")
            } else if (c4 <= c2 || c1 != c3 && chessboard[c4 - 1][c3] != blackPlayer && !blackPassant) { // only move to empty when enPassant
                invalidInput = true
                println("Invalid Input")
            } else if (c4 == c2 + 1 && chessboard[c4 - 1][c3] == blackPlayer &&
                c3 == c1 + 1 || // legal capturing move
                c4 == c2 + 1 && c3 == c1 - 1
            ) {
                black.beat()
                listOfWhitePawns[whitePawnCounter] = "b"
                whitePawnCounter++
                moveExecutioner(c1, c2, c3, c4)
            } else if (c1 == c3 && c4 == c2 + 1 && chessboard[c4 - 1][c3] == emptySlot) { //legal regular move
                if (c4 != 8) {
                    if (chessboard[c4][c3] == blackPlayer) {
                        if (c3 == 0) {
                            if (chessboard[c4][c3 + 1] == emptySlot) {
                                listOfWhitePawns[whitePawnCounter] = "s"
                                whitePawnCounter++
                            }
                        } else if (c3 == 8) {
                            if (chessboard[c4][c3 - 1] == emptySlot) {
                                listOfWhitePawns[whitePawnCounter] = "s"
                                whitePawnCounter++
                            }
                        } else {
                            listOfWhitePawns[whitePawnCounter] = "s"
                            whitePawnCounter++
                        }
                    }
                }
                moveExecutioner(c1, c2, c3, c4)
            } else if (c2 == whitePlayerStartingIndex && c4 == c2 + 2 && c1 == c3) { //double initial
                whitePassant = true
                moveExecutioner(c1, c2, c3, c4)
            } else {
                invalidInput = true
                println("Invalid Input")
            }
        } else {                                          // +-+-+PLAYER 2+-+-+
            if (chessboard[c2 - 1][c1] != blackPlayer) { // is player here?
                println("No black pawn at $currentCoords")
                invalidInput = true
            } else if (c1 == c3 && c2 <= c4) { // cannot move backward
                invalidInput = true
                println("Invalid Input")
            } else if (c4 >= c2 || c1 != c3 && chessboard[c4 - 1][c3] != whitePlayer && !whitePassant) { // only move to empty when enPassant
                invalidInput = true
                println("Invalid Input")
            } else if (c4 == c2 - 1 && chessboard[c4 - 1][c3] == whitePlayer &&
                c3 == c1 + 1 || // legal capturing move
                c4 == c2 - 1 && c3 == c1 - 1
            ) {
                white.beat()
                listOfBlackPawns[blackPawnCounter] = "b"
                blackPawnCounter++
                moveExecutioner(c1, c2, c3, c4)
            } else if (c1 == c3 && c4 == c2 - 1 && chessboard[c4 - 1][c3] == emptySlot) { //legal regular move
                if (c4 != 1) {
                    if (chessboard[c4][c3] == whitePlayer) {
                        if (c3 == 0) {
                            if (chessboard[c4][c3 + 1] == emptySlot) {
                                listOfBlackPawns[blackPawnCounter] = "s"
                                blackPawnCounter++
                            }
                        } else if (c3 == 8) {
                            if (chessboard[c4][c3 - 1] == emptySlot) {
                                listOfBlackPawns[blackPawnCounter] = "s"
                                blackPawnCounter++
                            }
                        } else {
                            listOfBlackPawns[blackPawnCounter] = "s"
                            blackPawnCounter++
                        }
                    }
                }
                moveExecutioner(c1, c2, c3, c4)
            } else if (c2 == blackPlayerStartingIndex && c4 == c2 - 2 && c1 == c3) { //double initial
                blackPassant = true
                moveExecutioner(c1, c2, c3, c4)
            } else {
                invalidInput = true
                println("Invalid Input")
            }
        }
    }

    fun turnHandler() {

        println("$currentTurn's turn:")
        val move = readLine()!!.lowercase()
        currentCoords = move[0].toString() + move[1].toString()
        if (move == "exit") { // End Game
            println("Bye!")
            currentTurn = "exit"
        } else if (move.contains(regex)) { // input is valid
            val c1Index = move[0]
            val c3Index = move[2]

            val c1 = files.indexOf(c1Index) // Coords
            val c2 = move[1].digitToInt()
            val c3 = files.indexOf(c3Index)
            val c4 = move[3].digitToInt()

            moveHandler(c1, c2, c3, c4)
        } else {                                // input is NOT valid
            println("Invalid Input")
            turnHandler()
        }
    }

    do { // Game loop
        if (!invalidInput) {
            printBoard()
        }
        turnHandler()
    } while (currentTurn != "exit")
}
