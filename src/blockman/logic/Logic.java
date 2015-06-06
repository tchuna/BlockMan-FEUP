package blockman.logic;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.blockman.data.Map;

public class Logic {
	private Map map;
	private PhysicsWorld physicsWorld;
	
	private boolean carringBox = false;
	private boolean win = false;
	
	public Logic(Map map,PhysicsWorld physicsWorld){
		this.map = map;
		this.physicsWorld = physicsWorld;
	}
	
	public Logic() {
	}

	public boolean remove_box_left(float player_pos_x, float player_pos_y, int mapStartX, int mapStartY, int spacing, Scene scene) {
		for(int i = 0; i < map.getHeight(); i++)
			for(int a = 0; a < map.getWidth(); a++){
				int pos_x = mapStartX + a * spacing;
				int pos_y = mapStartY - i * spacing;
				if(pos_x < player_pos_x && pos_x + 100 > player_pos_x)
					if(pos_y < player_pos_y && pos_y + 100 > player_pos_y){
						if(!map.isSomethingUp(a,i))
							if(map.getMap()[a][i].getKind() != "blank"){
								scene.detachChild(map.getMap()[a][i].getSprite());
								physicsWorld.destroyBody(map.getMap()[a][i].getBody());
								map.getMap()[a][i].getSprite().detachSelf();
								map.getMap()[a][i].getSprite().dispose();
								map.getMap()[a][i].setBlank();
								return true;
							}
					}
			}
		return false;
	}

	public boolean remove_box_right(float player_pos_x, float player_pos_y, int mapStartX, int mapStartY, int spacing, Scene scene) {
		for(int i = 0; i < map.getHeight(); i++)
			for(int a = 0; a < map.getWidth(); a++){
				int pos_x = mapStartX + a * spacing;
				int pos_y = mapStartY - i * spacing;
				if(pos_x - 100 < player_pos_x && pos_x > player_pos_x)
					if(pos_y < player_pos_y && pos_y + 100 > player_pos_y){
						if(!map.isSomethingUp(a,i))
							if(map.getMap()[a][i].getKind() != "blank"){
								scene.detachChild(map.getMap()[a][i].getSprite());
								physicsWorld.destroyBody(map.getMap()[a][i].getBody());
								map.getMap()[a][i].getSprite().detachSelf();
								map.getMap()[a][i].getSprite().dispose();
								map.getMap()[a][i].setBlank();
								return true;
							}
					}
			}
		return false;
	}
	
	public boolean leave_box_left(float player_pos_x, float player_pos_y, int mapStartX,
			int mapStartY, int spacing, Scene scene, ITextureRegion box_layer, VertexBufferObjectManager vertexBufferObjectManager, boolean vis) {
		for(int i = 0; i < map.getHeight(); i++)
			for(int a = 0; a < map.getWidth(); a++){
				int pos_x = mapStartX + a * spacing;
				int pos_y = mapStartY - i * spacing;
				if(pos_x - 100 < player_pos_x && pos_x > player_pos_x)
					if(pos_y < player_pos_y && pos_y + 100 > player_pos_y){
						int box_to_change = a - 1;
						int space = 100;
						if(player_pos_x - (pos_x - 100) <= 60){
							box_to_change = a - 2;
							space = 200;
						}
							if(map.getMap()[a - 1][i].getKind() != "rock" && map.getMap()[a - 1][i].getKind() != "box" )	
								if(map.getMap()[box_to_change][i].getKind() == "blank"){
									int y = map.getLowerY(box_to_change, i);
									int new_pos_y = mapStartY - y * spacing;
									map.getMap()[box_to_change][y].setSprite(new Sprite(pos_x-space, new_pos_y ,box_layer, vertexBufferObjectManager));
									map.getMap()[box_to_change][y].setKind("box");
									scene.attachChild(map.getMap()[box_to_change][y].getSprite());
									//Placing physics Body
									IShape box = new Rectangle(pos_x - space, new_pos_y, 100, 100, vertexBufferObjectManager);
									box.setVisible(vis);
									FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(10, 0, 0f);
									Body b = PhysicsFactory.createBoxBody(physicsWorld, (IAreaShape) box, BodyType.StaticBody, wallFixtureDef);
									b.getFixtureList().get(0).setUserData("box body");
									map.getMap()[box_to_change][y].setBody(b);

									return true;
								}
					}
			}
		return false;
	}

	public boolean leave_box_right(float player_pos_x, float player_pos_y, int mapStartX,
			int mapStartY, int spacing, Scene scene, ITextureRegion box_layer, VertexBufferObjectManager vertexBufferObjectManager, boolean vis) {
		for(int i = 0; i < map.getHeight(); i++)
			for(int a = 0; a < map.getWidth(); a++){
				int pos_x = mapStartX + a * spacing;
				int pos_y = mapStartY - i * spacing;
				if(pos_x - 100 < player_pos_x && pos_x > player_pos_x)
					if(pos_y < player_pos_y && pos_y + 100 > player_pos_y){
						if(map.getMap()[a][i].getKind() != "rock" && map.getMap()[a][i].getKind() != "box" )
						if(map.getMap()[a + 1][i].getKind() == "blank"){
							//Placing sprites
							int y = map.getLowerY(a + 1, i);
							int new_pos_y = mapStartY - y * spacing;
							map.getMap()[a + 1][y].setSprite(new Sprite(pos_x + 100, new_pos_y ,box_layer, vertexBufferObjectManager));
							map.getMap()[a + 1][y].setKind("box");
							scene.attachChild(map.getMap()[a + 1][y].getSprite());
							//Placing physics Body
							IShape box = new Rectangle(pos_x + 100, new_pos_y, 100, 100, vertexBufferObjectManager);
		                    box.setVisible(vis);
		                    FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 1f);
		                    Body b = PhysicsFactory.createBoxBody(physicsWorld, (IAreaShape) box, BodyType.StaticBody, wallFixtureDef);
		                    b.getFixtureList().get(0).setUserData("box body");
		                    map.getMap()[a + 1][y].setBody(b);
							return true;
						}
					}
			}
		return false;
	}
	
	public boolean getWin(){
		return win;
	}
	public void setWin(boolean a){
		win = a;
	}
	
	public boolean getCarringBox(){
		return carringBox;
	}
	
	public void setCarringBox(boolean a){
		carringBox = a;
	}

	public Body generatePlayer(Body player_body, float PLAYER_START_X, float PLAYER_START_Y, VertexBufferObjectManager vertex) {
		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
		final IShape player_shape = new Rectangle(PLAYER_START_X, PLAYER_START_Y, 40, 60, vertex);

		player_body = PhysicsFactory.createBoxBody(physicsWorld, (IAreaShape) player_shape, BodyType.DynamicBody,  playerFixtureDef);
		player_body.setFixedRotation(true); // prevent rotation
     
		//----------------------
		//Add onFloor sensor-----

		PolygonShape sensor = new PolygonShape();
		sensor.setAsBox((float)0.3, (float)1.2,new Vector2(0, 0), 0);
		player_body.createFixture(sensor, 0).setSensor(true);

		PolygonShape sensor_box = new PolygonShape();
		sensor_box.setAsBox((float)1, (float)0.3,new Vector2(0, 0), 0);
		player_body.createFixture(sensor_box, 0).setSensor(true);

		player_body.getFixtureList().get(2).setUserData("box sensor");
		player_body.getFixtureList().get(1).setUserData("feet");
		player_body.getFixtureList().get(0).setUserData("body");
		
		return player_body;
	}

	public void setMap(Map map2) {
		this.map = map2;
	}

	public void setPhysics(PhysicsWorld physicsWorld2) {
		this.physicsWorld = physicsWorld2;
	}
}
