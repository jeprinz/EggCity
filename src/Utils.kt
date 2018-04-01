fun <T> floodfill(things: Collection<T>, adjacent: (T, T) -> Boolean): Collection<Collection<T>>{
    val allThings = HashSet(things)
    val groups: HashSet<Collection<T>> = hashSetOf()
    while(allThings.isNotEmpty()){
        val firstThing = allThings.first()
        allThings.remove(firstThing)
        val group = floodColor(things, adjacent, firstThing)
        group.add(firstThing)
        groups.add(group)
        allThings.removeAll(group)
    }
    return groups
}

fun <T> floodColor(things: Collection<T>, adjacent: (T,T) -> Boolean, start: T) : HashSet<T>{
    val added = hashSetOf<T>()
    for (thing in things){
        if (adjacent(start, thing)){
            added.add(thing)
            added.addAll(floodColor(things.minus(start), adjacent, thing))
        }
    }
    return added
}

