package com.example.quizapp.data

import com.example.quizapp.data.Category.CS_BASICS
import com.example.quizapp.data.Category.NETWORKS
import com.example.quizapp.data.Category.PROGRAMMING

/** Built-in questions for version 1: 10 per category, 4 options each, one correct answer. */
object QuestionBank {

    val questions: List<Question> = listOf(
        // Programming
        Question(
            1, PROGRAMMING, "What is the output of the following code?",
            listOf("4", "5", "6", "23"), 1,
            code = "fun main() {\n    println(2 + 3)\n}",
        ),
        Question(2, PROGRAMMING, "Which keyword declares a read-only variable in Kotlin?", listOf("var", "val", "const", "let"), 1),
        Question(3, PROGRAMMING, "What does the \"null\" value represent?", listOf("The number zero", "An empty string", "The absence of a value", "The boolean false"), 2),
        Question(4, PROGRAMMING, "Which of the following is a loop in Kotlin?", listOf("if", "when", "for", "try"), 2),
        Question(5, PROGRAMMING, "What is the purpose of a constructor in a class?", listOf("To initialize the class properties", "To delete the class", "To inherit from another class", "To print the class name"), 0),
        Question(6, PROGRAMMING, "Which data type is used to store whole numbers?", listOf("String", "Int", "Double", "Boolean"), 1),
        Question(
            7, PROGRAMMING, "What is the output of the following code?",
            listOf("2.5", "2", "3", "2.0"), 1,
            code = "val result = 5 / 2\nprintln(result)",
        ),
        Question(8, PROGRAMMING, "Which keyword is used to define a function in Kotlin?", listOf("function", "def", "fun", "func"), 2),
        Question(
            9, PROGRAMMING, "What is the output of the following code?",
            listOf("123", "12", "0123", "321"), 0,
            code = "for (i in 1..3) {\n    print(i)\n}",
        ),
        Question(10, PROGRAMMING, "Which keyword is used to declare a class in Kotlin?", listOf("struct", "class", "new", "define"), 1),

        // Computer Networks
        Question(11, NETWORKS, "What does DNS stand for?", listOf("Domain Name System", "Data Network Service", "Digital Name Server", "Dynamic Network System"), 0),
        Question(12, NETWORKS, "How many layers does the OSI model have?", listOf("4", "5", "7", "9"), 2),
        Question(13, NETWORKS, "Which device forwards packets between different networks?", listOf("Switch", "Hub", "Router", "Repeater"), 2),
        Question(14, NETWORKS, "What is the default port number for HTTPS?", listOf("21", "80", "443", "25"), 2),
        Question(15, NETWORKS, "Which protocol provides reliable, connection-oriented delivery?", listOf("UDP", "TCP", "IP", "ICMP"), 1),
        Question(16, NETWORKS, "Which of the following is a valid IPv4 address?", listOf("192.168.1.1", "256.10.5.1", "192.168.1", "10.0.0.300"), 0),
        Question(17, NETWORKS, "Which OSI layer is responsible for routing?", listOf("Physical", "Data Link", "Network", "Transport"), 2),
        Question(18, NETWORKS, "What is the default port number for HTTP?", listOf("22", "53", "80", "110"), 2),
        Question(19, NETWORKS, "Which protocol automatically assigns IP addresses to devices?", listOf("DNS", "DHCP", "FTP", "SMTP"), 1),
        Question(20, NETWORKS, "How many bits are in an IPv4 address?", listOf("16", "32", "64", "128"), 1),

        // Computer Science Basics
        Question(21, CS_BASICS, "Which of the following is hardware?", listOf("Windows", "CPU", "Google Chrome", "Python"), 1),
        Question(22, CS_BASICS, "What is the main job of an operating system?", listOf("Managing hardware and software resources", "Designing web pages", "Editing photos", "Sending emails only"), 0),
        Question(23, CS_BASICS, "Which data structure follows the LIFO principle?", listOf("Queue", "Stack", "Array", "Tree"), 1),
        Question(24, CS_BASICS, "Which data structure follows the FIFO principle?", listOf("Stack", "Queue", "Graph", "Tree"), 1),
        Question(25, CS_BASICS, "What is the time complexity of binary search on a sorted array?", listOf("O(n)", "O(log n)", "O(n²)", "O(1)"), 1),
        Question(26, CS_BASICS, "What does SQL stand for?", listOf("Structured Query Language", "Simple Question Language", "Sequential Query Logic", "System Query Language"), 0),
        Question(27, CS_BASICS, "Which type of memory loses its data when the power is turned off?", listOf("ROM", "RAM", "SSD", "Hard disk"), 1),
        Question(28, CS_BASICS, "What is the binary number 1010 in decimal?", listOf("8", "10", "12", "5"), 1),
        Question(29, CS_BASICS, "In a database table, which key uniquely identifies each row?", listOf("Foreign key", "Primary key", "Index", "View"), 1),
        Question(30, CS_BASICS, "Which sorting algorithm repeatedly swaps adjacent elements that are out of order?", listOf("Bubble sort", "Merge sort", "Quick sort", "Heap sort"), 0),
    )

    fun questionsFor(category: Category): List<Question> = questions.filter { it.category == category }

    fun countFor(category: Category): Int = questions.count { it.category == category }
}
