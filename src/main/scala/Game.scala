package org.jousse
package bot
import scala.util.{ Try, Success, Failure }

case class Game(
    id: String,
    board: Board,
    hero1: Hero,
    hero2: Hero,
    hero3: Hero,
    hero4: Hero,
    turn: Int = 0) {

  def heroes = List(hero1, hero2, hero3, hero4)

  def hero: Hero = hero(turn % 4)
  def hero(number: Int): Hero = heroes lift number getOrElse hero1
  def hero(pos: Pos): Option[Hero] = heroes find (_.pos == pos)

  def step(update: Game => Game) = update(this).copy(turn = turn + 1)

  def withHero(f: Hero => Hero): Game = withHero(hero, f)

  def withHero(hero: Hero, f: Hero => Hero): Game = copy(
    hero1 = if (hero.number == 1) f(hero1) else hero1,
    hero2 = if (hero.number == 2) f(hero2) else hero2,
    hero3 = if (hero.number == 3) f(hero3) else hero3,
    hero4 = if (hero.number == 4) f(hero4) else hero4)

  override def toString = {

    val stringVector = board.tiles.zipWithIndex flatMap {
      case (xs, x) => xs.zipWithIndex map {
        case (tile, y) => {
          val s = hero(Pos(x, y)).fold(tile.render)(_.render)
          if (y == 0) s"|$s"
          else if (y == board.size - 1) s"$s|\n"
          else s
        }
      }
    }

    val line = "+" + "--" * board.size + "+\n"

    line + stringVector.mkString + line
  }
}
