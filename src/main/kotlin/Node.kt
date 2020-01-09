package org.sam.extractor.configuration;


data class Node (val name:String,
                 val children: MutableList<Node> = arrayListOf(),
                 val action: String = "",
                 val icon: String = "")
