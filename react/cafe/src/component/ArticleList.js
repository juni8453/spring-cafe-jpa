import useFetch from "../hook/useFetch";
import Article from "./Article";
import CreateArticle from "./CreateArticle";
import '../component-css/CreateArticle.css';
import '../component-css/ArticleTable.css';


export default function ArticleList() {
    const data = useFetch("http://3.36.169.168:8080/articles?sort=id,desc");
    return (<>

            <CreateArticle/>

        <div>
            <table className="type03">
                <tbody>
                <tr>
                    <td>{data.content && data.content.map(article => (
                    <Article article={article} key={article.id}/>
                        ))}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </>
    )
}
