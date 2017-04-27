class MoviesController < ApplicationController

  def movie_params
	params.require(:movie).permit(:title, :rating, :description, :release_date)
  end

  def show
	id = params[:id] # retrieve movie ID from URI route
	@movie = Movie.find(id) # look up movie by unique ID
	# will render app/views/movies/show.<extension> by default
  end

  def index
      @all_ratings = Movie.pluck(:rating).uniq()
      if not session[:ratings]
    	  session[:ratings] = Hash[@all_ratings.map { |i| [i, 1] }]
      end
      if not session[:orderby]
          session[:orderby] = ""
      end

      if not params[:ratings]
          # TODO: Finish this shiiiiiiiiiitt
      end



      if not params[:ratings]
          params[:ratings] = session[:ratings]
      end
      session[:ratings] = params[:ratings]
      filter = params[:ratings].keys
      if params[:orderby]
          if params[:orderby] == "title" and not params[:ratings]
              redirect_to movies_path({:orderby => params[:orderby], :ratings => session[:ratings]})
          end
    	  session[:orderby] = params[:orderby]
          @movies = Movie.where(:rating => filter).order(params[:orderby])
    	  @set = params[:orderby]
      elsif session[:orderby]
          params[:orderby] = session[:orderby]
          @movies = Movie.where(:rating => filter).order(params[:orderby])
    	  @set = params[:orderby]
      else
    	  @set = false
    	  @movies = Movie.where(:rating => filter)
      end
  end

  def new
	# default: render 'new' template
  end

  def create
	@movie = Movie.create!(movie_params)
	flash[:notice] = "#{@movie.title} was successfully created."
	redirect_to movies_path
  end

  def edit
	@movie = Movie.find params[:id]
  end

  def update
	@movie = Movie.find params[:id]
	@movie.update_attributes!(movie_params)
	flash[:notice] = "#{@movie.title} was successfully updated."
	redirect_to movie_path(@movie)
  end

  def destroy
	@movie = Movie.find(params[:id])
	@movie.destroy
	flash[:notice] = "Movie '#{@movie.title}' deleted."
	redirect_to movies_path
  end

end
