package nashorn

package object js {
  /** The undefined value. */
  @inline def undefined: js.UndefOr[Nothing] =
    ().asInstanceOf[js.UndefOr[Nothing]]

  /** Tests whether the given value is undefined. */
  @inline def isUndefined(v: scala.Any): Boolean =
    v.asInstanceOf[scala.AnyRef] eq undefined


}
