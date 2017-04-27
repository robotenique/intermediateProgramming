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
            if params[:orderby]
                session[:orderby] = params[:orderby]
                params[:ratings] = session[:ratings]
                redirect_to movies_path({:orderby => params[:orderby], :ratings => params[:ratings]})
            else
                params[:ratings] = session[:ratings]
                params[:orderby] = session[:orderby]
                redirect_to movies_path({:orderby => params[:orderby], :ratings => params[:ratings]})
            end
        else
            session[:ratings] = params[:ratings]
            if not params[:orderby]
                params[:orderby] = session[:orderby]
                redirect_to movies_path({:orderby => params[:orderby], :ratings => params[:ratings]})
            end
        end
        if params[:orderby]
            session[:orderby] = params[:orderby]
        end
        filter = params[:ratings].keys
        @movies = Movie.where(:rating => filter).order(params[:orderby])
        @set = params[:orderby]
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
