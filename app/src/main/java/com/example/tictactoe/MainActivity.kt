package com.example.tictactoe
import android.R.attr
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.tictactoe.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList
import android.R.attr.button
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity()
{

    enum class Turn
    {
        NOUGHT,
        CROSS
    }

    private var firstTurn = Turn.CROSS
    private var currentTurn = Turn.CROSS

    private var crossesScore = 0
    private var noughtsScore = 0

    private var boardList = mutableListOf<Button>()
    private var boardList2 = mutableListOf<Button>()

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBoard()
    }

    private fun initBoard()
    {
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)
        boardList2.add(binding.a11)
        boardList2.add(binding.a21)
        boardList2.add(binding.a31)
        boardList2.add(binding.b11)
        boardList2.add(binding.b21)
        boardList2.add(binding.b31)
        boardList2.add(binding.c11)
        boardList2.add(binding.c21)
        boardList2.add(binding.c31)
    }


    fun boardTapped(view: View)
    {
        currentTurn = Turn.CROSS
            if (view !is Button)
                return
            addToBoard(view)


            currentTurn = Turn.NOUGHT
            if (view in boardList)
                GlobalScope.launch {
                    autoPlay()
                }
            else if (view in boardList2)
                GlobalScope.launch {
                    autoPlay2()
                }



    }

    private fun checkForVictory(s: String): Boolean
    {
        //Horizontal Victory
        if((match(binding.a1,s) && match(binding.a2,s) && match(binding.a3,s))||(match(binding.a11,s) && match(binding.a21,s) && match(binding.a31,s)))
            return true
        if((match(binding.b1,s) && match(binding.b2,s) && match(binding.b3,s))||(match(binding.b11,s) && match(binding.b21,s) && match(binding.b31,s)))
            return true
        if((match(binding.c1,s) && match(binding.c2,s) && match(binding.c3,s))||(match(binding.c11,s) && match(binding.c21,s) && match(binding.c31,s)))
            return true


        //Vertical Victory
        if((match(binding.a1,s) && match(binding.b1,s) && match(binding.c1,s))||(match(binding.a11,s) && match(binding.b11,s) && match(binding.c11,s)))
            return true
        if((match(binding.a2,s) && match(binding.b2,s) && match(binding.c2,s))||(match(binding.a21,s) && match(binding.b21,s) && match(binding.c21,s)))
            return true
        if((match(binding.a3,s) && match(binding.b3,s) && match(binding.c3,s))||(match(binding.a31,s) && match(binding.b31,s) && match(binding.c31,s)))
            return true

        //Diagonal Victory
        if((match(binding.a1,s) && match(binding.b2,s) && match(binding.c3,s))||(match(binding.a11,s) && match(binding.b21,s) && match(binding.c31,s)))
            return true
        if((match(binding.a3,s) && match(binding.b2,s) && match(binding.c1,s))||(match(binding.a31,s) && match(binding.b21,s) && match(binding.c11,s)))
            return true
        return false
    }

    private fun match(button: Button, symbol : String): Boolean = button.text == symbol

    private fun result(title: String)
    {
        val message = "\nNoughts $noughtsScore\n\nCrosses $crossesScore"
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Reset")
            { _,_ ->
                resetBoard()
            }
            .setCancelable(false)
            .show()
    }

    private fun resetBoard()
    {
        for(button in boardList)
        {
            button.text = ""
        }
        for(button in boardList2)
        {
            button.text = ""
        }

        currentTurn = firstTurn
        setTurnLabel()
    }

    private fun fullBoard(): Boolean
    {
        for(button in boardList)
        {
            if(button.text == "")
                return false
        }
        return true
    }

    private fun addToBoard(button: Button)
    {
        if(button.text != "")
            return
        if(currentTurn == Turn.NOUGHT)
        {
            button.text = NOUGHT
            if(checkForVictory(NOUGHT))
            {
                noughtsScore++
                result("Noughts Win!")

            } else
                if(fullBoard())
                {
                    result("Draw")
                }
            //currentTurn = Turn.CROSS
        }
        else if(currentTurn == Turn.CROSS)
        {
            button.text = CROSS
            if(checkForVictory(CROSS))
            {
                crossesScore++
                result("Crosses Win!")

            } else
                if(fullBoard())
                {
                    result("Draw")
                }
            //currentTurn = Turn.NOUGHT
        }
        setTurnLabel()
    }

    private fun setTurnLabel()
    {
        var turnText = ""
        if(currentTurn == Turn.NOUGHT)
            turnText = "Turn $CROSS"
        else if(currentTurn == Turn.CROSS)
            turnText = "Turn $NOUGHT"

        binding.turnTV.text = turnText
    }

    companion object
    {
        const val NOUGHT = "O"
        const val CROSS = "X"
    }

    private fun autoPlay()
    {
        GlobalScope.launch(Dispatchers.IO)
        {
            var boardList3 = mutableListOf<Button>()
            var value = 0
            for (button in boardList) {
                button.isClickable = false
                if (button.text == "") {
                    value++
                    boardList3.add(button)
                }
            }
            if (value != 0) {
                val rnds = (0..value - 1).random()
                val randomElement = boardList3[rnds]
                boardList3.clear()
                val rnd = (1000..10000).random()
                delay(2000L+rnd)
                for (button in boardList)  button.isClickable = true
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    addToBoard(randomElement)
                })
            } else currentTurn = firstTurn
        }

    }
    private suspend fun autoPlay2()
    {
        GlobalScope.launch(Dispatchers.IO)
        {
            var boardList3 = mutableListOf<Button>()
            var value = 0
            for (button in boardList2) {
                button.isClickable = false
                if (button.text == "") {
                    value++
                    boardList3.add(button)
                }
            }

            if (value != 0) {
                val rnds = (0..value - 1).random()
                val randomElement = boardList3[rnds]
                boardList3.clear()
                val rnd = (1000..10000).random()
                delay(2000L+rnd)
                for (button in boardList2)  button.isClickable = true
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    addToBoard(randomElement)
                })
            } else currentTurn = firstTurn
        }


    }
}
