package br.pucpr.posjogos.trabalhofinal.renanfagundes;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class ArcanoidContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		
		Object a = contact.getFixtureA().getUserData();
		if (a != null) {
			GameObject ga = (GameObject)a;
			
			Object b = contact.getFixtureB().getUserData();
			if (b != null){
				GameObject gb = (GameObject)b;
				
				ga.OnCollisionEnter(gb);
				if (gb != null)
				gb.OnCollisionEnter(ga);
			}
		}
		
		
		
		
	}

	@Override
	public void endContact(Contact contact) {
		Object a = contact.getFixtureA().getUserData();
		if (a != null) {
			GameObject ga = (GameObject)a;
			
			Object b = contact.getFixtureB().getUserData();
			if (b != null){
				GameObject gb = (GameObject)b;
				
				ga.OnCollisionExit(gb);
				if (gb != null)
					gb.OnCollisionExit(ga);
			}
		}
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold arg1) {
		// TODO Auto-generated method stub
		
	}

}
