package com.ejemplo.myapp.ui.utils

object ExerciseTranslator {

    private val bodyPartMap = mapOf(
        "waist" to "Cintura",
        "chest" to "Pecho",
        "back" to "Espalda",
        "cardio" to "Cardio",
        "upper arms" to "Brazos",
        "lower arms" to "Antebrazos",
        "upper legs" to "Piernas (Muslo)",
        "lower legs" to "Piernas (Gemelo)",
        "shoulders" to "Hombros",
        "neck" to "Cuello"
    )

    private val equipmentMap = mapOf(
        "body weight" to "Peso corporal",
        "dumbbell" to "Mancuerna",
        "barbell" to "Barra",
        "kettlebell" to "Pesa rusa",
        "cable" to "Cable",
        "machine" to "Máquina",
        "medicine ball" to "Balón medicinal",
        "stability ball" to "Balón de estabilidad",
        "band" to "Banda elástica",
        "resistance band" to "Banda de resistencia",
        "bench" to "Banco",
        "smith machine" to "Máquina Smith",
        "ez barbell" to "Barra EZ",
        "rope" to "Cuerda",
        "weighted" to "Con peso",
        "leverage machine" to "Máquina de palanca",
        "assisted" to "Asistido",
        "medicine ball" to "Balón medicinal",
        "bosu ball" to "Bosu",
        "hammer strength" to "Máquina Hammer",
        "stability ball" to "Balón suizo",
        "tire" to "Neumático",
        "trap bar" to "Barra hexagonal",
        "roller" to "Rodillo",
        "stationary bike" to "Bicicleta estática",
        "elliptical machine" to "Elíptica",
        "rowing machine" to "Remo",
        "stepper" to "Step",
        "skierg machine" to "Skierg"
    )

    private val muscleMap = mapOf(
        "abs" to "Abdominales",
        "quads" to "Cuádriceps",
        "lats" to "Dorsales",
        "hamstrings" to "Isquiotibiales",
        "calves" to "Gemelos",
        "triceps" to "Tríceps",
        "biceps" to "Bíceps",
        "shoulders" to "Hombros",
        "chest" to "Pecho",
        "forearms" to "Antebrazos",
        "glutes" to "Glúteos",
        "adductors" to "Aductores",
        "abductors" to "Abductores",
        "traps" to "Trapecios",
        "upper back" to "Espalda superior",
        "lower back" to "Espalda baja",
        "neck" to "Cuello",
        "pectoralis major" to "Pectoral mayor",
        "deltoids" to "Deltoides",
        "serratus anterior" to "Serrato anterior"
    )

    private val instructionVerbs = mapOf(
        "lie" to "Túmbate",
        "stand" to "Ponte de pie",
        "sit" to "Siéntate",
        "hold" to "Sujeta",
        "grasp" to "Agarra",
        "place" to "Coloca",
        "position" to "Posiciona",
        "lift" to "Levanta",
        "raise" to "Eleva",
        "lower" to "Baja",
        "push" to "Empuja",
        "pull" to "Tira",
        "press" to "Presiona",
        "squeeze" to "Aprieta",
        "contract" to "Contrae",
        "extend" to "Extiende",
        "flex" to "Flexiona",
        "bend" to "Dobla",
        "rotate" to "Gira",
        "twist" to "Tuerce",
        "jump" to "Salta",
        "step" to "Da un paso",
        "keep" to "Mantén",
        "maintain" to "Mantén",
        "breathe" to "Respira",
        "exhale" to "Exhala",
        "inhale" to "Inhala",
        "repeat" to "Repite",
        "perform" to "Realiza",
        "complete" to "Completa",
        "ensure" to "Asegúrate de",
        "slowly" to "lentamente",
        "carefully" to "con cuidado",
        "your" to "tu",
        "the" to "el/la",
        "with" to "con",
        "and" to "y",
        "to" to "a",
        "from" to "desde",
        "for" to "durante",
        "at" to "en",
        "on" to "en",
        "back" to "espalda",
        "feet" to "pies",
        "hands" to "manos",
        "arms" to "brazos",
        "legs" to "piernas",
        "knees" to "rodillas",
        "elbows" to "codos",
        "shoulders" to "hombros",
        "chest" to "pecho",
        "hips" to "caderas",
        "head" to "cabeza",
        "neck" to "cuello",
        "floor" to "suelo",
        "ground" to "suelo",
        "bench" to "banco",
        "weight" to "peso",
        "bar" to "barra",
        "handle" to "manillar",
        "straight" to "recto/a",
        "up" to "arriba",
        "down" to "abajo",
        "upward" to "hacia arriba",
        "downward" to "hacia abajo",
        "forward" to "hacia adelante",
        "backward" to "hacia atrás",
        "side" to "lado",
        "start" to "inicio",
        "end" to "final"
    )

    private val instructionPhrases = mapOf(
        "face down" to "boca abajo",
        "face up" to "boca arriba",
        "one leg" to "una pierna",
        "on a foam roll" to "en un rodillo de espuma",
        "inner thigh" to "parte interna del muslo",
        "so that" to "de modo que",
        "as much weight" to "tanto peso",
        "as can be tolerated" to "como puedas tolerar",
        "one leg extended" to "una pierna extendida",
        "the other leg" to "la otra pierna",
        "into the air" to "hacia el aire",
        "starting position" to "posición inicial",
        "slowly lower" to "baja lentamente",
        "carefully place" to "coloca con cuidado",
        "back and forth" to "hacia adelante y hacia atrás",
        "side to side" to "de lado a lado",
        "up and down" to "de arriba a abajo",
        "shoulder width apart" to "a la anchura de los hombros",
        "flat on the floor" to "apoyado/a en el suelo",
        "keep your back straight" to "mantén la espalda recta",
        "bend your knees" to "dobla las rodillas",
        "exhale as you" to "exhala mientras",
        "inhale as you" to "inhala mientras",
        "repeat for the desired" to "repite las veces",
        "desired number of repetitions" to "número deseado de repeticiones",
        "range of motion" to "rango de movimiento",
        "at the top of the movement" to "en la parte superior del movimiento",
        "at the bottom of the movement" to "en la parte inferior del movimiento"
    )

    fun translateBodyPart(part: String): String = bodyPartMap[part.lowercase()] ?: part

    fun translateEquipment(equipment: String): String = equipmentMap[equipment.lowercase()] ?: equipment

    fun translateMuscle(muscle: String): String = muscleMap[muscle.lowercase()] ?: muscle

    fun translateDifficulty(difficulty: String): String = when (difficulty.lowercase()) {
        "beginner" -> "Principiante"
        "intermediate" -> "Intermedio"
        "advanced" -> "Avanzado"
        else -> difficulty
    }

    fun translateInstruction(instruction: String): String {
        if (instruction.isBlank()) return instruction
        
        var translated = instruction.lowercase()
        
        // Primero intentamos traducir frases completas (más precisas)
        instructionPhrases.forEach { (eng, esp) ->
            translated = translated.replace(eng, esp)
        }
        
        // Luego traducimos palabras sueltas para lo que quede
        val words = translated.split(" ")
        val finalWords = words.map { word ->
            val cleanWord = word.trim { !it.isLetter() }
            val translation = instructionVerbs[cleanWord]
            if (translation != null) {
                word.replace(cleanWord, translation, ignoreCase = true)
            } else {
                word
            }
        }
        
        val result = finalWords.joinToString(" ")
        // Intentar restaurar la primera mayúscula de la frase
        return result.replaceFirstChar { it.uppercase() }
    }

    fun translateExerciseName(name: String): String {
        // Algunos nombres comunes se pueden traducir directamente
        return name
            .replace("Push-up", "Flexión", ignoreCase = true)
            .replace("Pull-up", "Dominada", ignoreCase = true)
            .replace("Bench Press", "Press de banca", ignoreCase = true)
            .replace("Squat", "Sentadilla", ignoreCase = true)
            .replace("Deadlift", "Peso muerto", ignoreCase = true)
            .replace("Lunge", "Zancada", ignoreCase = true)
            .replace("Crunch", "Abdominal", ignoreCase = true)
            .replace("Plank", "Plancha", ignoreCase = true)
            .replace("Curls", "Curl", ignoreCase = true)
            .replace("Dumbbell", "con Mancuerna", ignoreCase = true)
            .replace("Barbell", "con Barra", ignoreCase = true)
            .replace("Kettlebell", "con Pesa Rusa", ignoreCase = true)
            .replace("Cable", "en Polea", ignoreCase = true)
            .trim()
    }
}
