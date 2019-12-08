/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shortestPath;

/**
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 27.01.2015
 */
public interface Heuristic<V> {
	/**
	 * Schätzt die Kosten (Distanz) von u nach v ab.
	 *
	 * @param u
	 * @param v
	 * @return Geschätzte Kosten.
	 */
	double estimatedCost(V u, V v);
}
